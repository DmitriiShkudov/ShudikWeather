package com.example.shkudikweatherapp.views.main_activity

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.Background

interface WindIcon : MvpView, Background {

    fun showWind(wind: Int)

}