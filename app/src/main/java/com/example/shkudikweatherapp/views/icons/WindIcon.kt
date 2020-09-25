package com.example.shkudikweatherapp.views.icons

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.WeatherStates

interface WindIcon : MvpView, WeatherStates {

    fun showWind(wind: Float)

}