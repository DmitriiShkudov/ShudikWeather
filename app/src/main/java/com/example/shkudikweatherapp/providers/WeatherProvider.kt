package com.example.shkudikweatherapp.providers

import android.content.Context

object WeatherProvider {

    private const val CITIES_PREF = "Countries preferences"
    private const val SELECTED_CITY_NAME = "Selected country name"
    private const val DEFAULT_CITY_NAME = "Paris"

    var context: Context? = null

    var selectedCity: String
    get() = this.context?.getSharedPreferences(CITIES_PREF, 0)?.
                getString(SELECTED_CITY_NAME, DEFAULT_CITY_NAME) ?: DEFAULT_CITY_NAME

    set(value) =
        this.context?.getSharedPreferences(CITIES_PREF, 0)?.edit()?.putString(SELECTED_CITY_NAME, value)?.apply()!!

    var temperature: Int = 0
    var wind: Int = 0
    var humidity: Int = 0
    var description: String = String()

}