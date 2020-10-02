package com.example.shkudikweatherapp.presenters.main_activity

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.weather_states.Background
import javax.inject.Inject

@InjectViewState
class BackgroundPresenter : MvpPresenter<Background>() {

    fun set() {

        when (WeatherProvider.weatherDesc) {

            WeatherProvider.WeatherDesc.LOW_SNOW -> viewState.lowSnow()
            WeatherProvider.WeatherDesc.SNOW -> viewState.snow()
            WeatherProvider.WeatherDesc.LOW_CLOUD -> viewState.lowCloudy()
            WeatherProvider.WeatherDesc.CLOUD -> viewState.cloudy()
            WeatherProvider.WeatherDesc.CLEAR -> viewState.clear()
            WeatherProvider.WeatherDesc.RAIN -> viewState.rainy()
            WeatherProvider.WeatherDesc.HUMID -> viewState.humid()

        }

    }

}