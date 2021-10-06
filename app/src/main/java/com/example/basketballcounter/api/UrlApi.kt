package com.example.basketballcounter.api

import retrofit2.Call
import retrofit2.http.GET

interface UrlApi {

    @GET("/")
    fun fetchContents(): Call<String>
}