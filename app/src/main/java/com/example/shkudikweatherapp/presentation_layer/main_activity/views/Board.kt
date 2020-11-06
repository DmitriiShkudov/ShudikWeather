package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.graphics.Typeface
import android.widget.Toast
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.Helper.KEY_BOARD_CODE_5
import com.example.shkudikweatherapp.data_layer.providers.Helper.Objects.location
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedCity
import com.example.shkudikweatherapp.presentation_layer.common_protocols.IError
import kotlinx.android.synthetic.main.activity_main.*

interface Board: IError {

    fun setOnEnterClickEvent()
    fun setUserLocationTitle()
    fun setCity()
    fun setCity(specified: String)

}

class BoardImpl(private val activity: MainActivity) : Board {

    override fun setOnEnterClickEvent() { with(activity) {

            input_city.setOnEditorActionListener { textView, i, keyEvent ->

                if (i == KEY_BOARD_CODE_5)
                    activity.viewModel.applyCity(this)

                true
            }
        }
    }

    override fun setUserLocationTitle() { with(activity) {

            input_city.setText(location)
            input_city.setTypeface(null, Typeface.ITALIC)

        }
    }

    override fun setCity() =
        activity.input_city.also {it.setTypeface(null, Typeface.NORMAL)}.setText(selectedCity)

    override fun setCity(specified: String) =
        activity.input_city.also {it.setTypeface(null, Typeface.NORMAL)}.setText(specified)

    override fun showError(message: String) =
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

}