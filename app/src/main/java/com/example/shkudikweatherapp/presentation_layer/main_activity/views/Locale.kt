package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.view.View
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import kotlinx.android.synthetic.main.activity_main.*

interface Locale {

    fun setLocale()

}

class LocaleImpl(private val activity: MainActivity) : Locale {

    override fun setLocale() {

        with(activity) {

            fun setLocaleText(obj: View) {

                when (UserPreferences.language) {

                    UserPreferences.Language.RUS -> when (obj) {

                        text_temp -> text_temp.text = "Температура"
                        text_wind -> text_wind.also { it.textSize = 16f }.text = "Скорость ветра"
                        text_humidity -> text_humidity.text = "Влажность"
                        input_city -> input_city.hint = "Город"
                        text_bad_connection -> text_bad_connection.text = "Плохое соединение..."

                    }

                    UserPreferences.Language.GER -> when (obj) {

                        text_temp -> text_temp.text = "Temperatur"
                        text_wind -> text_wind.also { it.textSize = 13f }.text =
                            "Windgeschwindigkeit"
                        text_humidity -> text_humidity.text = "Feuchtigkeit"
                        input_city -> input_city.hint = "Stadt"
                        text_bad_connection -> text_bad_connection.text = "Schlechte Verbindung..."

                    }

                    UserPreferences.Language.ENG -> when (obj) {

                        text_temp -> text_temp.text = "Temperature"
                        text_wind -> text_wind.also { it.textSize = 16f }.text = "Wind speed"
                        text_humidity -> text_humidity.text = "Humidity"
                        input_city -> input_city.hint = "City"
                        text_bad_connection -> text_bad_connection.text = "Bad connection..."

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