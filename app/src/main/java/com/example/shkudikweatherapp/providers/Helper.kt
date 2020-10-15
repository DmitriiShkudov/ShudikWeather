package com.example.shkudikweatherapp.providers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.states.MainDescription
import com.example.shkudikweatherapp.states.WeatherState

object Helper {

    // User Preferences

    const val PERCENT = "%"
    const val METER_PER_SEC = " m/s"
    const val METER_PER_SEC_RUS = " м/с"
    const val ABS_ZERO = 273
    const val CITY_NOT_FOUND = "This city is not found"
    const val USER_PREF = "User pref"
    const val NOTIF_CITY_KEY = "City key"
    const val NOTIF_INTERVAL_KEY = "Interval key"
    const val LANG_KEY = "Language key"
    const val FULLSCREEN_KEY = "Fullscreen key"
    const val DEGREE_KEY = "Degree key"
    const val SEARCH_MODE_KEY = "Search mode key"
    const val EMPTY = ""

    // Weather Provider

    const val WEATHER_PREF = "Weather preferences"
    const val SELECTED_CITY_NAME_KEY = "Selected country name"
    const val DEFAULT_CITY_NAME = "Paris"
    const val HELP_KEY = "Help_"
    const val MAX_HELP_CITIES = 3

    // MA

    const val OVERCAST_ENG = "rcast"
    const val OVERCAST_RUS = "пасм"
    const val OVERCAST_GER = "Bedeckt"
    const val LOW_ENG = "light"
    const val LOW_RUS = "небольшой"
    const val LOW_GER = "iger"
    const val EMPTY_INPUT_ERROR = "Enter the city!"

    // Retrofit Client

    const val BASE_URL_WEATHER = "http://api.openweathermap.org/"
    const val BASE_URL_TIME = "http://worldclockapi.com/"
    const val KEY_API = "6a8c6db6e5c6f3972d7ae682ae812b52"

    // Methods

    fun Int.fahrenheit() = this * 9/5 + 32

    fun <T> MutableLiveData<T>.value(value: T) {

        this.value = value

    }

    fun isNightTime(time: String) =
        (time[0].toString() + time[1].toString()) == "23" || (time[1] == ':' && time[0].toString().toInt() in 0..5)

    fun getMainDescription(isNight: Boolean, string: String): MainDescription {

        if (!isNight) {

            listOf(
                MainDescription.THUNDERSTORM,
                MainDescription.DRIZZLE,
                MainDescription.RAIN,
                MainDescription.CLEAR,
                MainDescription.MIST,
                MainDescription.SMOKE,
                MainDescription.HAZE,
                MainDescription.DUST,
                MainDescription.FOG,
                MainDescription.SAND,
                MainDescription.ASH,
                MainDescription.SQUALL,
                MainDescription.TORNADO,
                MainDescription.CLOUDS,
                MainDescription.SNOW
            ).forEach {

                if (string == it.string) return it

            }

            throw Exception(RECEIVED_DESCRIPTION_IS_NOT_EXIST)

        } else {

            listOf(
                MainDescription.THUNDERSTORM_NIGHT,
                MainDescription.DRIZZLE_NIGHT,
                MainDescription.RAIN_NIGHT,
                MainDescription.CLEAR_NIGHT,
                MainDescription.MIST_NIGHT,
                MainDescription.SMOKE_NIGHT,
                MainDescription.HAZE_NIGHT,
                MainDescription.DUST_NIGHT,
                MainDescription.FOG_NIGHT,
                MainDescription.SAND_NIGHT,
                MainDescription.ASH_NIGHT,
                MainDescription.SQUALL_NIGHT,
                MainDescription.TORNADO_NIGHT,
                MainDescription.CLOUDS_NIGHT,
                MainDescription.SNOW_NIGHT
            ).forEach {

                if (string == it.string) return it

            }

            throw Exception(RECEIVED_DESCRIPTION_IS_NOT_EXIST)

        }

    }

    // Exception messages
    const val RECEIVED_DESCRIPTION_IS_NOT_EXIST = "Received description isn't exist"


}