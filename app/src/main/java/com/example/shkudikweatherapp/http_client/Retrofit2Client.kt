package com.example.shkudikweatherapp.http_client

import com.example.shkudikweatherapp.viewmodels.MainViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit2Client(val viewModel: MainViewModel) {

    private val weatherService: WeatherService

        get() {

            val retrofit =
                Retrofit.Builder().
                baseUrl(com.example.shkudikweatherapp.http_client.RetrofitClient.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build()

            return retrofit.create(WeatherService::class.java)

        }

    private val BASE_URL = "http://api.openweathermap.org/"
    private val KEY_API = "6a8c6db6e5c6f3972d7ae682ae812b52"
    private val EXISTED_CITY = "London"


    fun loadWeather(city: String) {

        runBlocking {

            val call = weatherService.getWeather(
                city = city,
                appid = KEY_API,
                lang = "en"
            )

            val weather = call.execute().body()

            if (weather != null) {

                viewModel.weatherLoaded(weather)

            } else {

                viewModel.connectionError()

            }
        }

    }

}