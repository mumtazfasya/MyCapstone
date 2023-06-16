package com.dicoding.capthree.data


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("product")
    val product: List<Product>,
    @SerializedName("success")
    val success: Int
)