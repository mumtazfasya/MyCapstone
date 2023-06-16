package com.dicoding.capthree

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.capthree.MainActivity.Companion.PRODUCTREC
import com.dicoding.capthree.databinding.ActivityFormBinding
import com.dicoding.capthree.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import com.dicoding.capthree.data.Product

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class FormActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormBinding
    private lateinit var dialog : Dialog
    private var recommendationRes : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFormBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialog = Dialog(this)


        val pref = UserPreferences.getInstance(dataStore)
        val theme = ViewModelProvider(this, ThemeFactory(pref))[ThemeViewModel::class.java]
        theme.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setupSimpleSpinner()
        binding.button.setOnClickListener {
            startDetection()
        }

    }
    private fun startDetection(){
        //deteksi
        val model = Model.newInstance(this)
        val recommendation = getReccommendation()

        //Color
        val selectedValueColor = binding.simpleSpinner.selectedItem.toString()
        val floatValue = when (selectedValueColor) {
            "White" -> 5f
            "Black" -> 0f
            "Mustard" -> 3f
            "Sage" -> 4f
            "Grey" -> 1f
            else -> 2f
        }
        val byteBufferColor = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        byteBufferColor.putFloat(floatValue)
        byteBufferColor.rewind()

        //Quality
        val selectedValueQuality = binding.simpleSpinner2.selectedItem.toString()
        val floatValueQuality = when (selectedValueQuality) {
            "Cotton Combed" -> 0f
            "Spandex" -> 2f
            else -> 1f
        }
        val byteBufferQuality = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        byteBufferQuality.putFloat(floatValueQuality)
        byteBufferQuality.rewind()

        //Size
        val selectedValueSize = binding.simpleSpinner3.selectedItem.toString()
        val floatValueSize = when (selectedValueSize) {
            "S" -> 2f
            "M" -> 0f
            "L" -> 1f
            "XL" -> 3f
            else -> 4f
        }
        val byteBufferSize = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        byteBufferSize.putFloat(floatValueSize)
        byteBufferSize.rewind()

        //Thickness
        val selectedValueThickness = binding.simpleSpinner4.selectedItem.toString()
        val floatValueThickness = when (selectedValueThickness) {
            "0" -> 0f
            "20s" -> 1f
            "30s" -> 2f
            else -> 1f
        }
        val byteBufferThickness = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        byteBufferThickness.putFloat(floatValueThickness)
        byteBufferThickness.rewind()

        //prepare input
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBufferColor)
        val inputFeature1 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(byteBufferQuality)
        val inputFeature2 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature2.loadBuffer(byteBufferSize)
        val inputFeature3 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputFeature3.loadBuffer(byteBufferThickness)

        val outputs = model.process(inputFeature0, inputFeature1, inputFeature2, inputFeature3)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var maxIndex = 0
        var maxValue = outputFeature0.getFloatValue(0)
        for (i in 0 until 150){
            val value = outputFeature0.getFloatValue(i)
            if (value > maxValue){
                maxValue = value
                maxIndex = i
            }
        }
        recommendationRes = recommendation[maxIndex]

        showDialogBarcode()

    }

    private fun getReccommendation(): List<String> {
        val inputString = this.assets.open("labels.txt").bufferedReader().use {
            it.readText()
        }

        return inputString.split("\n")
    }

    private fun setupSimpleSpinner() {

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.colors,
            android.R.layout.simple_spinner_item
        )

        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.quality,
            android.R.layout.simple_spinner_item
        )

        val adapter3 = ArrayAdapter.createFromResource(
            this,
            R.array.size,
            android.R.layout.simple_spinner_item
        )

        val adapter4 = ArrayAdapter.createFromResource(
            this,
            R.array.thickness,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.simpleSpinner.adapter = adapter
        binding.simpleSpinner2.adapter = adapter2
        binding.simpleSpinner3.adapter = adapter3
        binding.simpleSpinner4.adapter = adapter4

        binding.simpleSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedItem = parent!!.getItemAtPosition(position)
                Toast.makeText(this@FormActivity, "$selectedItem Selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun showDialogBarcode() {
        dialog.setContentView(R.layout.alert_recommend)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val data= recommendationRes.split("|")
        dialog.findViewById<TextView>(R.id.item_name).text = data[0]
        Glide.with(this@FormActivity).load("https://storage.googleapis.com/easychicb/public/images/" + data[1]).into(dialog.findViewById<ImageView>(R.id.img_recommend))

        dialog.findViewById<Button>(R.id.goTo).setOnClickListener {
            val product = Product(data[3], 1, data[1], data[0], data[2], data[4], data[5], data[6])
            val intent = Intent(this@FormActivity, MainActivity::class.java)
            intent.putExtra(PRODUCTREC, product)
            startActivity(intent)
        }

        dialog.show()
    }
}