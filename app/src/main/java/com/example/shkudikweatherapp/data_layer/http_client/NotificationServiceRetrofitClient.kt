package com.example.shkudikweatherapp.data_layer.http_client

import com.example.shkudikweatherapp.data_layer.http_client.RetrofitClient.Data.retrofitTime
import com.example.shkudikweatherapp.data_layer.http_client.RetrofitClient.Data.retrofitWeather
import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.states.States

class NotificationServiceRetrofitClient : RetrofitClient() {

    override val timeUTCService: TimeUTCService
        get() = retrofitTime.create(TimeUTCService::class.java)

    override val weatherService: WeatherService
        get() = retrofitWeather.create(WeatherService::class.java)

    override fun loadWeather(city: String) = try {

            weatherService.getWeather(
                city = city,
                appid = Helper.KEY_API,
                lang = UserPreferences.language.apiStr
            ).execute().body()

        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }


    override fun loadTimeUTC() = try {

            timeUTCService.getTimeUTC().execute().body()

        } catch (e: Throwable) { null }


    override fun loadForecast(latitude: Float, longitude: Float) = null
    override fun loadForecast(city: String): Forecast? = null
    override fun loadWeather(latitude: Float, longitude: Float) = null

}