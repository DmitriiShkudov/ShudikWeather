package com.example.shkudikweatherapp.data_layer.providers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.degreeUnit
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.Language.*
import com.example.shkudikweatherapp.data_layer.enums.MainDescription.*
import com.example.shkudikweatherapp.data_layer.providers.Helper.Units.windUnit

object Helper {

    var context: Context? = null

    private fun requireContext() = context ?: throw Error(CONTEXT_NOT_INIT)

    const val KEY_BOARD_CODE_5 = 5
    const val KEY_BOARD_CODE_6 = 6


    // Loading delay
    const val LOADING_DELAY: Long = 10000

    // Notifications

    const val NOTIFICATION_CHANNEL = "W"

    // User Preferences

    const val ABS_ZERO = 273
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
    const val NOTIFICATION_CITY_NAME_KEY = "Notification city name"
    const val SELECTED_LAT_KEY = "Selected lat"
    const val SELECTED_LON_KEY = "Selected lon"
    const val DEFAULT_CITY_NAME = "Paris"
    const val HELP_KEY = "Help_"
    const val MAX_HELP_CITIES = 3

    // Retrofit Client

    const val BASE_URL_WEATHER = "http://api.openweathermap.org/"
    const val BASE_URL_TIME = "http://worldclockapi.com/"
    const val KEY_API = "6a8c6db6e5c6f3972d7ae682ae812b52"

    // Methods

    fun View.showKeyboard(context: Context) =

        (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
        showSoftInput(this, 0)


    fun View.hideKeyboard(context: Context) =

        (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
        hideSoftInputFromWindow(this.windowToken, 0)


    fun EditText.reformat() {

        val text = this.text.toString()
        this.setText("${text[0].toUpperCase()}${text.toLowerCase().substring(1..text.length - 1)}")

    }


    fun View.setSafeOnClickListener(action: () -> Unit) = this.setOnClickListener {
        action.invoke()
        this.setOnClickListener(null)
    }


    fun setTemp(intTemp: Int): String {

        val temp = if (degreeUnit == UserPreferences.TemperatureUnit.DEG_C)
            (intTemp - ABS_ZERO).toString() else
            ((intTemp - ABS_ZERO).fahrenheit().toString())

        return temp + degreeUnit.str
    }


    fun <T> setWindSpeed(wind: T) =
        wind.toString() + windUnit

    fun setWindDirection(deg: Int) = when (deg) {

        in 0..15, in 345..360 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_eastern_rus)
            ENG -> context?.getString(R.string.wind_dir_eastern_eng)
            GER -> context?.getString(R.string.wind_dir_eastern_ger)

        }

        in 15..75 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_north_east_rus)
            ENG -> context?.getString(R.string.wind_dir_north_east_eng)
            GER -> context?.getString(R.string.wind_dir_north_east_ger)

        }

        in 75..105 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_northern_rus)
            ENG -> context?.getString(R.string.wind_dir_northern_eng)
            GER -> context?.getString(R.string.wind_dir_northern_ger)

        }

        in 105..165 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_north_west_rus)
            ENG -> context?.getString(R.string.wind_dir_north_west_eng)
            GER -> context?.getString(R.string.wind_dir_north_west_ger)

        }

        in 165..195 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_western_rus)
            ENG -> context?.getString(R.string.wind_dir_western_eng)
            GER -> context?.getString(R.string.wind_dir_western_ger)

        }

        in 195..255 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_south_west_rus)
            ENG -> context?.getString(R.string.wind_dir_south_west_eng)
            GER -> context?.getString(R.string.wind_dir_south_west_ger)

        }

        in 255..285 -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_southern_rus)
            ENG -> context?.getString(R.string.wind_dir_southern_eng)
            GER -> context?.getString(R.string.wind_dir_southern_ger)

        }

        else -> when (language) {

            RUS -> context?.getString(R.string.wind_dir_north_east_rus)
            ENG -> context?.getString(R.string.wind_dir_north_east_eng)
            GER -> context?.getString(R.string.wind_dir_north_east_ger)

        }

    }

    fun setPressure(intPressure: Int) = when (language) {

        RUS -> (intPressure / 1.333).toInt().toString() + " " + context?.getString(R.string.pressure_unit_rus)
        else -> (intPressure / 1.333).toInt().toString() + " " + context?.getString(R.string.pressure_unit_eng_ger)

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


    fun Int.fahrenheit() = this * 9 / 5 + 32

    fun <T> MutableLiveData<T>.value(value: T) {

        this.value = value

    }


    fun isNightTime(time: String) =
        (time[0].toString() + time[1].toString()) == "21" ||
        (time[0].toString() + time[1].toString()) == "22" ||
        (time[0].toString() + time[1].toString()) == "23" ||
        (time[1] == ':' && time[0].toString().toInt() in 0..5)


    fun getMainDescription(isNight: Boolean, string: String): MainDescription {

        if (!isNight) {

            listOf(
                THUNDERSTORM,
                DRIZZLE,
                RAIN,
                CLEAR,
                MIST,
                SMOKE,
                HAZE,
                DUST,
                FOG,
                SAND,
                ASH,
                SQUALL,
                TORNADO,
                CLOUDS,
                SNOW
            ).forEach {

                if (string == it.string) return it

            }

            throw Exception(RECEIVED_DESCRIPTION_IS_NOT_EXIST)

        } else {

            listOf(
                THUNDERSTORM_NIGHT,
                DRIZZLE_NIGHT,
                RAIN_NIGHT,
                CLEAR_NIGHT,
                MIST_NIGHT,
                SMOKE_NIGHT,
                HAZE_NIGHT,
                DUST_NIGHT,
                FOG_NIGHT,
                SAND_NIGHT,
                ASH_NIGHT,
                SQUALL_NIGHT,
                TORNADO_NIGHT,
                CLOUDS_NIGHT,
                SNOW_NIGHT
            ).forEach {

                if (string == it.string) return it

            }

            throw Exception(RECEIVED_DESCRIPTION_IS_NOT_EXIST)
        }
    }

    // Exception messages
    const val RECEIVED_DESCRIPTION_IS_NOT_EXIST = "Received description isn't exist"
    const val CONTEXT_NOT_INIT = "Context wasn't initialized"

    // Messages

    object Messages {

        val cityIsAlreadyAttachedToNotificationsMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_notification_is_already_attached_rus)
                ENG -> requireContext().getString(R.string.message_notification_is_already_attached_eng)
                GER -> requireContext().getString(R.string.message_notification_is_already_attached_ger)

            }

        val notificationsWereResetMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_notification_is_reset_rus)
                ENG -> requireContext().getString(R.string.message_notification_is_reset_eng)
                GER -> requireContext().getString(R.string.message_notification_is_reset_ger)

            }

        val restartMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_restart_rus)
                ENG -> requireContext().getString(R.string.message_restart_eng)
                GER -> requireContext().getString(R.string.message_restart_ger)

            }

        val successMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_successful_rus)
                ENG -> requireContext().getString(R.string.message_successful_eng)
                GER -> requireContext().getString(R.string.message_successful_ger)

            }

        val cityNotExistsMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_city_is_not_exists_rus)
                ENG -> requireContext().getString(R.string.message_city_is_not_exists_eng)
                GER -> requireContext().getString(R.string.message_city_is_not_exists_ger)

            }

        val emptyInputMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_empty_city_input_rus)
                ENG -> requireContext().getString(R.string.message_empty_city_input_eng)
                GER -> requireContext().getString(R.string.message_empty_city_input_ger)

            }

        val locationIsDeniedMessage: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_location_was_denied_by_user_rus)
                ENG -> requireContext().getString(R.string.message_location_was_denied_by_user_eng)
                GER -> requireContext().getString(R.string.message_location_was_denied_by_user_ger)

            }

        val locationIsUnavailable: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.message_location_is_unavailable_rus)
                ENG -> requireContext().getString(R.string.message_location_is_unavailable_eng)
                GER -> requireContext().getString(R.string.message_location_is_unavailable_ger)

            }

    }

    object Objects {

        val location: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.location_rus)
                ENG -> requireContext().getString(R.string.location_eng)
                GER -> requireContext().getString(R.string.location_ger)

            }

        val restart: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.restart_btn_rus)
                ENG -> requireContext().getString(R.string.restart_btn_eng)
                GER -> requireContext().getString(R.string.restart_btn_ger)

            }

        val cancel: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.cancel_btn_rus)
                ENG -> requireContext().getString(R.string.cancel_btn_eng)
                GER -> requireContext().getString(R.string.cancel_btn_ger)

            }

        val humidity: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.humidity_rus)
                ENG -> requireContext().getString(R.string.humidity_eng)
                GER -> requireContext().getString(R.string.humidity_ger)

            }

        val direction: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.direction_rus)
                ENG -> requireContext().getString(R.string.direction_eng)
                GER -> requireContext().getString(R.string.direction_ger)

            }

        val wind: String
            get() = when (language) {

                RUS -> requireContext().getString(R.string.wind_rus)
                ENG -> requireContext().getString(R.string.wind_eng)
                GER -> requireContext().getString(R.string.wind_ger)

            }

    }

    object Units {

        val windUnit: String
            get() = when (language) {

                RUS -> " " + requireContext().getString(R.string.meter_per_sec_rus)
                ENG -> " " + requireContext().getString(R.string.meter_per_sec_eng_ger)
                GER -> " " + requireContext().getString(R.string.meter_per_sec_eng_ger)

            }
    }
}