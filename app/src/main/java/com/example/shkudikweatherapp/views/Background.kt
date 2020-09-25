package com.example.shkudikweatherapp.views

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.WeatherStates

interface Background : MvpView, WeatherStates {

    fun clearBack()
    fun cloudyBack()
    fun rainyBack()

}