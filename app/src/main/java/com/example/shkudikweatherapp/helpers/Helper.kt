package com.example.shkudikweatherapp.helpers

import com.example.shkudikweatherapp.states.WeatherState

object Helper {

    const val PERCENT = "%"
    const val METER_PER_SEC = " m/s"
    const val ABS_ZERO = 273

    fun Int.fahrenheit() = (this*9/5+32)

    fun funnyDescDetermination(description: String) =

        when {

            description.contains("rain") ||
            description.contains("Regen") ||
            description.contains("дождь") ||
            description.contains("ливень") -> WeatherState.RAIN

            description.contains("clear") ||
            description.contains("ясно") ||
            description.contains("Himmel")-> WeatherState.CLEAR

            description.contains("overcast clouds") ||
            description.contains("пасмурно") ||
            description.contains("Bedeckt")-> WeatherState.CLOUDY

            description.contains("cloud") ||
            description.contains("обл") ||
            description.contains("Wolk") ||
            description.contains("bew")-> WeatherState.LOW_CLOUDY

            description.contains("haze") ||
            description.contains("fog") ||
            description.contains("mist") ||
            description.contains("мгла") ||
            description.contains("туман") ||
            description.contains("Dunst") -> WeatherState.HUMID

            description.contains("light") &&
            description.contains("snow")
            -> WeatherState.LOW_SNOW

            else -> WeatherState.SNOW

    }

}