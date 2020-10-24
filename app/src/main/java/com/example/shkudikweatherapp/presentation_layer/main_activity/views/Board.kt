package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.graphics.Typeface
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import kotlinx.android.synthetic.main.activity_main.*

interface Board {

    fun setUserLocationTitle()
    fun setCity()
    fun setCity(specified: String)

}

class BoardImpl(private val activity: MainActivity) : Board {

    override fun setUserLocationTitle() { with(activity) {

            input_city.setText(
                when (language) {
                    UserPreferences.Language.RUS -> Helper.YOUR_LOCATION_RUS
                    UserPreferences.Language.ENG -> Helper.YOUR_LOCATION_ENG
                    UserPreferences.Language.GER -> Helper.YOUR_LOCATION_GER
                })
            input_city.setTypeface(null, Typeface.ITALIC)
        }

    }

    override fun setCity() =
        activity.input_city.also {it.setTypeface(null, Typeface.NORMAL)}.setText(WeatherProvider.selectedCity)

    override fun setCity(specified: String) =
        activity.input_city.also {it.setTypeface(null, Typeface.NORMAL)}.setText(specified)

}