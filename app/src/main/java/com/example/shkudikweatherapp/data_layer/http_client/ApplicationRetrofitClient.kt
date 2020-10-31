package com.example.shkudikweatherapp.data_layer.http_client

import com.example.shkudikweatherapp.data_layer.http_client.RetrofitClient.Data.retrofitTime
import com.example.shkudikweatherapp.data_layer.http_client.RetrofitClient.Data.retrofitWeather
import com.example.shkudikweatherapp.data_layer.providers.Helper.KEY_API
import com.example.shkudikweatherapp.data_layer.providers.Helper.cityNotFoundDesc
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainViewModel


class ApplicationRetrofitClient(private val viewModel: MainViewModel) : RetrofitClient() {

    override val weatherService: WeatherService
        get() = retrofitWeather.create(WeatherService::class.java)

    override val timeUTCService: TimeUTCService
        get() = retrofitTime.create(TimeUTCService::class.java)

    override fun loadWeather(city: String) =

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

                    viewModel.state.postValue(MainStates.UPDATED)
                    viewModel.state.postValue(MainStates.WRONG_CITY)
                    isSelectedCityExists = false
                    null

                }

            }

        } catch (e: Throwable) {

            viewModel.state.postValue(MainStates.BAD_CONNECTION)
            null

        }

    override fun loadWeather(latitude: Float, longitude: Float) =

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

                    viewModel.state.postValue(MainStates.UPDATED)
                    viewModel.state.postValue(MainStates.WRONG_CITY)

                    null

                }

            }

        } catch (e: Throwable) {

            viewModel.state.postValue(MainStates.BAD_CONNECTION)
            null

        }

    override fun loadForecast(city: String) =

        try {
            weatherService.getForecast(city = city, appid = KEY_API, lang = language.apiStr).
                execute().
                body()

        } catch (e: Throwable) {

            null

        }

    override fun loadForecast(latitude: Float, longitude: Float) =

        try {

            weatherService.getForecast(latitude, longitude , appid = KEY_API, lang = language.apiStr).
                execute().
                body()

        } catch (e: Throwable) {

            null

        }

    override fun loadTimeUTC() =

        try {

            timeUTCService.getTimeUTC().execute().body()

        } catch (e: Throwable) {

            viewModel.state.postValue(MainStates.BAD_CONNECTION)
            null

        }

}