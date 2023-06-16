package com.dicoding.capthree

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.capthree.data.ApiConfig
import com.dicoding.capthree.data.Product
import com.dicoding.capthree.data.ProductResponse
import com.dicoding.capthree.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvProduk.layoutManager = layoutManager

        val product = intent.getParcelableExtra<Product>(PRODUCTREC)!!
        Glide.with(this)
            .load("https://storage.googleapis.com/easychicb/public/images/"+product.image)
            .into(binding.itemPhoto)
        binding.itemName.text = product.name

        binding.cardView.setOnClickListener {
            val intentDetail = Intent(this, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.PRODUCT, product)
            startActivity(intentDetail)
        }

        displauProduct()

        val pref = UserPreferences.getInstance(dataStore)
        val theme = ViewModelProvider(this, ThemeFactory(pref))[ThemeViewModel::class.java]
        theme.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.article -> {
                startActivity(Intent(this, ArticleActivity::class.java))
            }
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listProduct(assetManager: AssetManager, fileName: String) : String?{
        var json: String? = null
        try {
            val inputStream: InputStream = assetManager.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }

    fun displauProduct() {
        val client = ApiConfig.getApi().getProduct()
        client.enqueue(object : Callback<ProductResponse>{
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val adapter = ProductAdapter(responseBody.product)
                        binding.rvProduk.adapter = adapter
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val PRODUCTREC = "product"
    }
}