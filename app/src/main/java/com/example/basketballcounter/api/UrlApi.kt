package com.example.basketballcounter.api

import retrofit2.Call
import retrofit2.http.GET

interface UrlApi {

    @GET(
        "data/2.5/weather?zip=01609," +
                "us" +
                "&appid=4bf6fa35a0efd77da636ced62b3aaa47")
    fun fetchContents(): Call<String>
}