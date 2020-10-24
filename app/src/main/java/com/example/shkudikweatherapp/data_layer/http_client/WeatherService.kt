package com.example.shkudikweatherapp.data_layer.http_client

import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") city: String,
                   @Query("appid") appid: String,
                   @Query("lang") lang: String): Call<Weather>

    @GET("/data/2.5/weather")
    fun getLocalWeather(@Query("lat") latitude: Float,
                        @Query("lon") longitude: Float,
                        @Query("appid") appid: String,
                        @Query("lang") lang: String): Call<Weather>

    @GET("/data/2.5/forecast")
    fun getForecast(@Query("q") city: String,
                    @Query("appid") appid: String,
                    @Query("lang") lang: String): Call<Forecast>

    @GET("/data/2.5/forecast")
    fun getForecast(@Query("lat") latitude: Float,
                    @Query("lon") longitude: Float,
                    @Query("appid") appid: String,
                    @Query("lang") lang: String): Call<Forecast>

}