package com.example.shkudikweatherapp.data_layer.http_client

import com.example.shkudikweatherapp.data_layer.providers.Helper.BASE_URL_TIME
import com.example.shkudikweatherapp.data_layer.providers.Helper.BASE_URL_WEATHER
import com.example.shkudikweatherapp.data_layer.providers.Helper.KEY_API
import com.example.shkudikweatherapp.data_layer.providers.Helper.cityNotFoundDesc
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.data_layer.states.States
import com.example.shkudikweatherapp.presentation_layer.viewmodels.MainViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    fun loadWeather(city: String) =

        try {

            with(
                weatherService.getWeather(city = city, appid = KEY_API, lang = language.apiStr).execute()
                    .body()
            ) {

                if (this != null) {

                    isSelectedCityExists = true
                    this

                } else {

                    viewModel.desc.postValue(cityNotFoundDesc)

                    viewModel.state.postValue(States.UPDATED)
                    viewModel.state.postValue(States.WRONG_CITY)
                    isSelectedCityExists = false
                    null

                }

            }

        } catch (e: Throwable) {

            viewModel.state.postValue(States.BAD_CONNECTION)
            null

        }

    fun loadForecast(city: String) =

        try {
            weatherService.getForecast(city = city, appid = KEY_API, lang = language.apiStr).
                execute().
                body()

        } catch (e: Throwable) {

            null

        }

    fun loadForecast(latitude: Float, longitude: Float) =

        try {

            weatherService.getForecast(latitude, longitude , appid = KEY_API, lang = language.apiStr).
                execute().
                body()

        } catch (e: Throwable) {

            null

        }


    fun loadLocalWeather(latitude: Float, longitude: Float) =

        try {

            with(
                weatherService.getLocalWeather(latitude = latitude,
                                               longitude = longitude,
                                               appid = KEY_API,
                                               lang = language.apiStr).execute().body()
            ) {

                if (this != null) {

                    this

                } else {

                    viewModel.state.postValue(States.UPDATED)
                    viewModel.state.postValue(States.WRONG_CITY)

                    null

                }

            }

        } catch (e: Throwable) {

            viewModel.state.postValue(States.BAD_CONNECTION)
            null

        }

    fun loadTimeUTC() =

        try {

            timeUTCService.getTimeUTC().execute().body()

        } catch (e: Throwable) {

            viewModel.state.postValue(States.BAD_CONNECTION)
            null

        }

}