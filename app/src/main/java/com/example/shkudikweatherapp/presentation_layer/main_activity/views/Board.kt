package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.graphics.Typeface
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.locationTitle
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import kotlinx.android.synthetic.main.activity_main.*

interface Board {

    fun setOnEnterClickEvent()
    fun setUserLocationTitle()
    fun setCity()
    fun setCity(specified: String)

}

class BoardImpl(private val activity: MainActivity) : Board {

    private companion object {

        const val CODE_ENTER = 5

    }

    override fun setOnEnterClickEvent() { with(activity) {

            input_city.setOnEditorActionListener { textView, i, keyEvent ->

                if (i == CODE_ENTER) {

                    activity.viewModel.applyCity(this)

                }
                true
            }
        }
    }

    override fun setUserLocationTitle() { with(activity) {

            input_city.setText(locationTitle)
            input_city.setTypeface(null, Typeface.ITALIC)

        }
    }

    override fun setCity() =
        activity.input_city.also {it.setTypeface(null, Typeface.NORMAL)}.setText(WeatherProvider.selectedCity)

    override fun setCity(specified: String) =
        activity.input_city.also {it.setTypeface(null, Typeface.NORMAL)}.setText(specified)

}