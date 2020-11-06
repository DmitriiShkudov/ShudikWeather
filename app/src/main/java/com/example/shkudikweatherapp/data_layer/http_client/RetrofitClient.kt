package com.example.shkudikweatherapp.data_layer.http_client

import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
import com.example.shkudikweatherapp.data_layer.providers.Helper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class RetrofitClient {

    protected object Data  {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(2500, TimeUnit.MILLISECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

        val retrofitWeather =
            Retrofit.Builder().
            baseUrl(Helper.BASE_URL_WEATHER).
            client(okHttpClient).
            addConverterFactory(GsonConverterFactory.create()).
            build()

        val retrofitTime =
            Retrofit.Builder().
            baseUrl(Helper.BASE_URL_TIME).
            client(okHttpClient).
            addConverterFactory(GsonConverterFactory.create()).
            build()

    }

    abstract val weatherService: WeatherService
    abstract val timeUTCService: TimeUTCService

    abstract fun loadWeather(city: String): Weather?
    abstract fun loadWeather(latitude: Float, longitude: Float): Weather?

    abstract fun loadForecast(city: String): Forecast?
    abstract fun loadForecast(latitude: Float, longitude: Float): Forecast?

    abstract fun loadTimeUTC(): TimeUTC?

}