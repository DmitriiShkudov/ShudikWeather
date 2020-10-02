package com.example.shkudikweatherapp.views.main_activity

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.weather_states.Background

interface TempIcon : MvpView, Background {

    fun showTemp(temp: Int)

}