package com.example.shkudikweatherapp.weather_states

import com.arellomobile.mvp.MvpView

interface Background : MvpView {

    fun lowSnow()
    fun snow()
    fun clear()
    fun cloudy()
    fun lowCloudy()
    fun rainy()
    fun humid()

}