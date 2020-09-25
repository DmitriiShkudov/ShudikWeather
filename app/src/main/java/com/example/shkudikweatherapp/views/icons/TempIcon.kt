package com.example.shkudikweatherapp.views.icons

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.WeatherStates

interface TempIcon : MvpView, WeatherStates {

    fun showTemp(temp: Float)

}