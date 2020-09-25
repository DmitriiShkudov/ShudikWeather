package com.example.shkudikweatherapp.views.icons

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.WeatherStates

interface HumidityIcon : MvpView, WeatherStates {

    fun showHumidity(humidity: Int)

}