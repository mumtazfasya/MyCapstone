package com.dicoding.capthree.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("colour")
    val colour: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("quality")
    val quality: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("thickness")
    val thickness: String,
) : Parcelable