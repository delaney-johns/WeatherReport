package com.ait.weatherreport.api

import retrofit2.http.Query
import com.ait.weatherreport.data.Base
import retrofit2.Call
import retrofit2.http.GET

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun getWeatherDetails(@Query("q") city: String,
                          @Query("units") units: String,
                          @Query("appid") appid: String): Call<Base>
}