package com.example.shkudikweatherapp.presenters

import android.os.Handler
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.activities.MainActivity
import com.example.shkudikweatherapp.client.HttpClient
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.views.BtnChangeCity

@InjectViewState
class BtnChangeCityPresenter : MvpPresenter<BtnChangeCity>() {

    companion object {

        const val SUCCESS_UPDATE = 1

    }

    private val handler = Handler {

        when (it.what) {

            SUCCESS_UPDATE -> {

                val receivedWeatherInfo = it.obj as HashMap<*, *>

                viewState.showResult(success = true)

                WeatherProvider.temperature = (receivedWeatherInfo["temp"] as String).toFloat().toInt()
                WeatherProvider.description = receivedWeatherInfo["description"] as String
                WeatherProvider.humidity = (receivedWeatherInfo["humidity"] as String).toInt()
                WeatherProvider.wind = (receivedWeatherInfo["wind"] as String).toFloat().toInt()

                Log.d("IN PRESENTER H-R", "Всё по кайфу")
                Log.d("туманность полученная", WeatherProvider.humidity.toString())

            }

            else -> {

                viewState.showResult(success = false)

            }

        }

        true

    }

    init {

        HttpClient.handlerForUpdate = this.handler

    }

    fun enteringCity() = viewState.enteringCity()

    fun applyCity() = viewState.applyCity()

    fun executeAndShowResult(city: String) {

        WeatherProvider.selectedCity = city
        HttpClient.loadWeatherInfo(city = city)

    }

}