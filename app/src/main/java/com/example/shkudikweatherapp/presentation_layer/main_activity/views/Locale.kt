package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.view.View
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.Language.*
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Locale
import kotlinx.android.synthetic.main.activity_main.*

class LocaleImpl(private val activity: MainActivity) : Locale {

    override fun setLocale() {

        with(activity) {

            fun setLocaleText(obj: View) {

                when (language) {

                    RUS -> when (obj) {

                        text_temp -> text_temp.text = getString(R.string.temperature_rus)
                        text_wind -> text_wind.text = getString(R.string.wind_rus)
                        text_humidity -> text_humidity.text = getString(R.string.humidity_rus)
                        input_city -> input_city.hint = getString(R.string.enter_city_hint_rus)
                        text_bad_connection -> text_bad_connection.text = getString(R.string.message_bad_connection_rus)

                    }

                    GER -> when (obj) {

                        text_temp -> text_temp.text = getString(R.string.temperature_ger)
                        text_wind -> text_wind.text = getString(R.string.wind_ger)
                        text_humidity -> text_humidity.text = getString(R.string.humidity_ger)
                        input_city -> input_city.hint = getString(R.string.enter_city_hint_ger)
                        text_bad_connection -> text_bad_connection.text = getString(R.string.message_bad_connection_ger)

                    }

                    ENG -> when (obj) {

                        text_temp -> text_temp.text = getString(R.string.temperature_eng)
                        text_wind -> text_wind.text = getString(R.string.wind_eng)
                        text_humidity -> text_humidity.text = getString(R.string.humidity_eng)
                        input_city -> input_city.hint = getString(R.string.enter_city_hint_eng)
                        text_bad_connection -> text_bad_connection.text = getString(R.string.message_bad_connection_eng)

                    }
                }
            }

            setLocaleText(text_temp)
            setLocaleText(text_wind)
            setLocaleText(text_humidity)
            setLocaleText(input_city)
            setLocaleText(text_bad_connection)

        }
    }
}