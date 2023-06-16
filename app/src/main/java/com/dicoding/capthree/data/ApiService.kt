package com.dicoding.capthree.data

import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
  @GET("product")
  fun getProduct(
  ): Call<ProductResponse>
}