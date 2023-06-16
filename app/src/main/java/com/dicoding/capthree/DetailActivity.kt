package com.dicoding.capthree

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dicoding.capthree.databinding.ActivityDetailBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.dicoding.capthree.data.Product

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)


        val product= intent.getParcelableExtra<Product>(PRODUCT)!!

        Glide.with(this)
            .load("https://storage.googleapis.com/easychicb/public/images/"+product.image)
            .into(binding.imgItemPhoto)
        binding.itemName.text = product.name
        binding.descHargaDetail.text = product.price
        binding.descBahan.text = product.quality
        binding.descUkuranDetail.text = product.size
        binding.descKetebalanDetail.text = product.thickness

        binding.buttonBayar.setOnClickListener {
            showDialogBarcode()
        }
    }

    private fun showDialogBarcode() {
        dialog.setContentView(R.layout.alert_barcode)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val text = binding.itemName.text.toString()
        val writer = QRCodeWriter()

        try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            dialog.findViewById<ImageView>(R.id.barcode).setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        dialog.show()
    }

    companion object {
        const val PRODUCT = "product"
    }
}