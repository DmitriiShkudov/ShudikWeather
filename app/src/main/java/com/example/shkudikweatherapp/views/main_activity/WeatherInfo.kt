package com.example.shkudikweatherapp.views.main_activity

import com.arellomobile.mvp.MvpView

interface WeatherInfo : MvpView {

    fun showWeather(description: String,
                    humidity: Int,
                    temp: Int,
                    wind: Int)

    fun showError()

}