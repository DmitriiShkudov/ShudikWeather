package com.example.shkudikweatherapp.http_client

import android.util.Log
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://api.openweathermap.org/"
    private const val KEY_API = "6a8c6db6e5c6f3972d7ae682ae812b52"
    private const val EXISTED_CITY = "London"


    val isConnected: Boolean
    get() = CoroutineScope(IO).async { this.testRequest(EXISTED_CITY) }


    val weatherService: WeatherService
    get() {

        val retrofit  = Retrofit.Builder().
                            baseUrl(BASE_URL).
                            addConverterFactory(GsonConverterFactory.create()).
                            build()

        return retrofit.create(WeatherService::class.java)

    }


    suspend fun loadWeather(city: String) {

         val call = weatherService.getWeather(
             city = city,
             appid = KEY_API,
             lang = "en"
         )

         call.enqueue(object : Callback<Weather> {

             override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                 val weather = response.body()
                 WeatherProvider.temperature = weather!!.main.temp.toInt()
                 WeatherProvider.description = weather.weather!![0].description
                 WeatherProvider.humidity = weather.main.humidity
                 WeatherProvider.wind = weather.wind.speed.toInt()
                 WeatherProvider.selectedCity = city

             }

             override fun onFailure(call: Call<Weather>, t: Throwable) {}


         })


    }

    fun testRequest(testCity: String): Boolean {

            val call = weatherService.getWeather(
                city = testCity,
                appid = KEY_API,
                lang = "en"
            )

        call.enqueue(object : Callback<Weather> {

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                val weather = response.body()

                

            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {}



    }

}