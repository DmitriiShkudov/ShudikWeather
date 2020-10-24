package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

interface WeatherIcons {

    fun setTemperature(temp: String)
    fun setHumidity(humidity: String)
    fun setWindSpeed(windSpeed: String)
    fun setDescription(description: String)

}

class WeatherIconsImpl(private val activity: MainActivity) : WeatherIcons {

    override fun setTemperature(temp: String) = activity.tvTemp.setText(temp)

    override fun setHumidity(humidity: String) = activity.tvHumidity.setText(humidity)

    override fun setWindSpeed(windSpeed: String) = activity.tvWind.setText(windSpeed)

    override fun setDescription(description: String) = activity.tvDescriptionIcon.setText(description)

}