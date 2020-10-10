package com.example.shkudikweatherapp.http_client

import android.util.Log
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.viewmodels.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Retrofit2Client(val viewModel: MainViewModel) {

    private val BASE_URL = "http://api.openweathermap.org/"
    private val KEY_API = "6a8c6db6e5c6f3972d7ae682ae812b52"


    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(1500, TimeUnit.MILLISECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val weatherService: WeatherService

        get() {

            val retrofit =
                Retrofit.Builder().
                baseUrl(BASE_URL).
                client(okHttpClient).
                addConverterFactory(GsonConverterFactory.create()).
                build()

            return retrofit.create(WeatherService::class.java)

        }


    fun getLang() = when (UserPreferences.language) {

        UserPreferences.Language.RUS -> "ru"
        UserPreferences.Language.ENG -> "en"
        UserPreferences.Language.GER -> "de"

    }

    fun loadWeather(city: String) {

        weatherService.getWeather(city = city, appid = KEY_API, lang = getLang()).enqueue(object : Callback<Weather> {

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                val weather = response.body()

                if (weather != null) {

                    viewModel.weatherLoaded(weather)

                } else {

                    viewModel.wrongCity()

                }

            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {

                viewModel.connectionError()

            }

        })

    }

}