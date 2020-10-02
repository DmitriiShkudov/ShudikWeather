package com.example.shkudikweatherapp.views.main_activity

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.Background

interface HumidityIcon : MvpView, Background {

    fun showHumidity(humidity: Int)

}