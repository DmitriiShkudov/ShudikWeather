package com.example.shkudikweatherapp.http_client

import com.example.shkudikweatherapp.providers.Helper
import com.example.shkudikweatherapp.providers.Helper.BASE_URL_TIME
import com.example.shkudikweatherapp.providers.Helper.BASE_URL_WEATHER
import com.example.shkudikweatherapp.providers.Helper.KEY_API
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.viewmodels.MainViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class Retrofit2Client(private val viewModel: MainViewModel) {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(1500, TimeUnit.MILLISECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val retrofitWeather =
        Retrofit.Builder().
        baseUrl(BASE_URL_WEATHER).
        client(okHttpClient).
        addConverterFactory(GsonConverterFactory.create()).
        build()

    private val retrofitTime =
        Retrofit.Builder().
        baseUrl(BASE_URL_TIME).
        client(okHttpClient).
        addConverterFactory(GsonConverterFactory.create()).
        build()

    private val weatherService: WeatherService
    get() = retrofitWeather.create(WeatherService::class.java)

    private val timeUTCService: TimeUTCService
    get() = retrofitTime.create(TimeUTCService::class.java)

    // essential determination for successful API request
    fun getLang() = when (UserPreferences.language) {

        UserPreferences.Language.RUS -> "ru"
        UserPreferences.Language.ENG -> "en"
        UserPreferences.Language.GER -> "de"

    }

    fun loadWeather(city: String) =

        try {

            with(
                weatherService.getWeather(city = city, appid = KEY_API, lang = getLang()).execute()
                    .body()
            ) {

                if (this != null) {

                    this

                } else {

                    viewModel.desc.postValue(Helper.CITY_NOT_FOUND)
                    viewModel.stateWrongCity()
                    null

                }

            }

        } catch (e: SocketTimeoutException) {

            viewModel.stateConnectionError()
            null

        }

    fun loadTimeUTC() =

        try {

            timeUTCService.getTimeUTC().execute().body()

        } catch (e: SocketTimeoutException) {

            viewModel.stateConnectionError()
            null

        }

}