package com.example.materialandroid.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface POTDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey:String,
        @Query("thumbs") thumbs:String = "true"
    ):Call<POTDResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey:String,
        @Query("date") date:String,
        @Query("thumbs") thumbs:String = "true"
    ):Call<POTDResponseData>


}