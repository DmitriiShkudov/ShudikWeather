package com.example.shkudikweatherapp.http_client

import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.UserPreferences
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") city: String,
                   @Query("appid") appid: String,
                   @Query("lang") lang: String): Call<Weather>

}