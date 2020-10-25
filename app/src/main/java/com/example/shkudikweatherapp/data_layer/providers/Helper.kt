package com.example.shkudikweatherapp.data_layer.providers

import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.degreeUnit
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.presentation_layer.states.MainDescription

object Helper {

    // User Preferences

    const val PRESSURE_RUS = " мм.рт.ст."
    const val PRESSURE = " mmHg"
    const val PERCENT = "%"
    const val METER_PER_SEC = " m/s"
    const val METER_PER_SEC_RUS = " м/с"
    const val ABS_ZERO = 273
    const val CITY_NOT_FOUND_RUS = "Город не найден"
    const val CITY_NOT_FOUND_ENG = "City is not found"
    const val CITY_NOT_FOUND_GER = "Stadt nicht gefunden"
    const val USER_PREF = "User pref"
    const val NOTIF_CITY_KEY = "City key"
    const val NOTIF_INTERVAL_KEY = "Interval key"
    const val LANG_KEY = "Language key"
    const val FULLSCREEN_KEY = "Fullscreen key"
    const val DEGREE_KEY = "Degree key"
    const val SEARCH_MODE_KEY = "Search mode key"
    const val LOCATION_APPLIED_KEY = "Location applied key"
    const val EMPTY = ""

    // Weather Provider

    const val WEATHER_PREF = "Weather preferences"
    const val SELECTED_CITY_NAME_KEY = "Selected city name"
    const val SELECTED_LAT_KEY = "Selected lat"
    const val SELECTED_LON_KEY = "Selected lon"
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
    const val EMPTY_INPUT_ERROR_RUS = "Введите город"
    const val EMPTY_INPUT_ERROR_ENG = "Enter the city"
    const val EMPTY_INPUT_ERROR_GER = "Stadt betreten"
    const val MINUS = "-"
    const val YOUR_LOCATION_RUS = "Локализация"
    const val YOUR_LOCATION_ENG = "Your location"
    const val YOUR_LOCATION_GER = "Ihr Standort"
    const val LOCATION_DENIED_WARNING_RUS = "Разрешение на запрос локации было отменено пользователем." +
            "Дайте разрешение в настройках вашего устройства или перезапустите приложение."
    const val LOCATION_DENIED_WARNING_ENG = "Accessibility to location was denied by user. " +
            "Allow location requests in your device settings or restart the application"
    const val LOCATION_DENIED_WARNING_GER = "Die Berechtigung zum Anfordern eines Standorts wurde vom Benutzer widerrufen. " +
            "Geben Sie die Berechtigung in Ihren Geräteeinstellungen ein oder installieren Sie die App neu."

    // Retrofit Client

    const val BASE_URL_WEATHER = "http://api.openweathermap.org/"
    const val BASE_URL_TIME = "http://worldclockapi.com/"
    const val KEY_API = "6a8c6db6e5c6f3972d7ae682ae812b52"

    // Methods

    fun setTemp(intTemp: Int): String {

        val temp = if (degreeUnit == UserPreferences.DegreeUnit.DEG_C)
            (intTemp - ABS_ZERO).toString() else
            ((intTemp - ABS_ZERO).fahrenheit().toString())

        return temp + degreeUnit.str
    }


    fun <T> setWindSpeed(wind: T) =

        wind.toString() +
                if (language != UserPreferences.Language.RUS) METER_PER_SEC else METER_PER_SEC_RUS

    fun setWindDirection(deg: Int) = when (deg) {

        in 0..15, in 345..360 -> when (language) {

            UserPreferences.Language.RUS -> "восточное"
            UserPreferences.Language.ENG -> "eastern"
            UserPreferences.Language.GER -> "östlich"

        }

        in 15..75 -> when (language) {

            UserPreferences.Language.RUS -> "северо-восточное"
            UserPreferences.Language.ENG -> "northeast"
            UserPreferences.Language.GER -> "nordost"

        }

        in 75..105 -> when (language) {

            UserPreferences.Language.RUS -> "северное"
            UserPreferences.Language.ENG -> "northern"
            UserPreferences.Language.GER -> "nord"

        }

        in 105..165 -> when (language) {

            UserPreferences.Language.RUS -> "северо-западное"
            UserPreferences.Language.ENG -> "northwest"
            UserPreferences.Language.GER -> "nordwest"

        }

        in 165..195 -> when (language) {

            UserPreferences.Language.RUS -> "западное"
            UserPreferences.Language.ENG -> "western"
            UserPreferences.Language.GER -> "western"

        }

        in 195..255 -> when (language) {

            UserPreferences.Language.RUS -> "юго-западное"
            UserPreferences.Language.ENG -> "southwestern"
            UserPreferences.Language.GER -> "südwestlich"

        }

        in 255..285 -> when (language) {

            UserPreferences.Language.RUS -> "южное"
            UserPreferences.Language.ENG -> "southern"
            UserPreferences.Language.GER -> "süd"

        }

        else -> when (language) {

            UserPreferences.Language.RUS -> "юго-восточное"
            UserPreferences.Language.ENG -> "southeast"
            UserPreferences.Language.GER -> "Süd-Ost"

        }

    }


    fun setPressure(intPressure: Int) = when (language) {

        UserPreferences.Language.RUS -> (intPressure / 1.333).toInt().toString() + PRESSURE_RUS
        else -> (intPressure / 1.333).toInt().toString() + PRESSURE

    }


    fun countTime(time: String, hours: Int) =

        if (time[1] == ':')

            (time[0].toString().toInt() + hours).toString() + time.substring(1 until time.length)

        else
            if (((time[0].toString() + time[1].toString()).toInt() + hours) >= 24)

                (((time[0].toString() + time[1].toString()).toInt() + hours) - 24).toString() +
                        time.substring(2 until time.length)
            else
                (((time[0].toString() + time[1].toString()).toInt() + hours)).toString() +
                        time.substring(2 until time.length)


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