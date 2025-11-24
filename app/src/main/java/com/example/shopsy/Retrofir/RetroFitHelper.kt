package com.example.shopsy.Retrofir

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitHelper {
    private const val BASE_URL = "https://dummyjson.com/"
    private var apiService: ApiService? = null
    fun getInstance(): ApiService {
        if (apiService == null) {
            apiService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        return apiService!!
    }
}