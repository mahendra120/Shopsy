package com.example.shopsy.Retrofir

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    fun getTodo(): Call<Tododata>

}