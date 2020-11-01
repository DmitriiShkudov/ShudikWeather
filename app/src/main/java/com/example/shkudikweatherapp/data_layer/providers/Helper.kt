package com.example.shkudikweatherapp.data_layer.providers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.degreeUnit
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.Language.*
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import com.example.shkudikweatherapp.data_layer.providers.Helper.GPS_ERROR_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.GPS_ERROR_RUS
import com.example.shkudikweatherapp.data_layer.providers.Helper.RESTART_MESSAGE_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.RESTART_MESSAGE_GER
import com.example.shkudikweatherapp.data_layer.providers.Helper.RESTART_MESSAGE_RUS

object Helper {

    const val KEY_BOARD_CODE_5 = 5
    const val KEY_BOARD_CODE_6 = 6

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
    const val NOTIFICATION_CITY_NAME_KEY = "Notification city name"
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
    const val YOUR_LOCATION_RUS = "Локация"
    const val YOUR_LOCATION_ENG = "Your location"
    const val YOUR_LOCATION_GER = "Ihr Standort"
    const val LOCATION_DENIED_WARNING_RUS =
        "Разрешение на запрос локации было отменено пользователем." +
                "Дайте разрешение в настройках вашего устройства или перезапустите приложение."
    const val LOCATION_DENIED_WARNING_ENG = "Accessibility to location was denied by user. " +
            "Allow location requests in your device settings or restart the application"
    const val LOCATION_DENIED_WARNING_GER =
        "Die Berechtigung zum Anfordern eines Standorts wurde vom Benutzer widerrufen. " +
                "Geben Sie die Berechtigung in Ihren Geräteeinstellungen ein oder installieren Sie die App neu."

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
        wind.toString() + if (language != RUS) METER_PER_SEC else METER_PER_SEC_RUS


    fun setWindDirection(deg: Int) = when (deg) {

        in 0..15, in 345..360 -> when (language) {

            RUS -> "восточное"
            ENG -> "eastern"
            GER -> "östlich"

        }

        in 15..75 -> when (language) {

            RUS -> "северо-восточное"
            ENG -> "northeast"
            GER -> "nordost"

        }

        in 75..105 -> when (language) {

            RUS -> "северное"
            ENG -> "northern"
            GER -> "nord"

        }

        in 105..165 -> when (language) {

            RUS -> "северо-западное"
            ENG -> "northwest"
            GER -> "nordwest"

        }

        in 165..195 -> when (language) {

            RUS -> "западное"
            ENG -> "western"
            GER -> "western"

        }

        in 195..255 -> when (language) {

            RUS -> "юго-западное"
            ENG -> "southwestern"
            GER -> "südwestlich"

        }

        in 255..285 -> when (language) {

            RUS -> "южное"
            ENG -> "southern"
            GER -> "süd"

        }

        else -> when (language) {

            RUS -> "юго-восточное"
            ENG -> "southeast"
            GER -> "Süd-Ost"

        }

    }


    fun setPressure(intPressure: Int) = when (language) {

        RUS -> (intPressure / 1.333).toInt().toString() + PRESSURE_RUS
        else -> (intPressure / 1.333).toInt().toString() + PRESSURE

    }


    fun getDrawable(context: Context, resId: Int) = ResourcesCompat.getDrawable(
        context.resources,
        resId,
        null)


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
        (time[0].toString() + time[1].toString()) == "22" || (time[0].toString() + time[1].toString()) == "23"
                || (time[1] == ':' && time[0].toString().toInt() in 0..5)


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

    // Other messages
    const val RESTART_MESSAGE_ENG = "To apply changes, you need to restart the application"
    const val RESTART_MESSAGE_RUS = "Чтобы применить изменения, необходимо перезагрузить приложение"
    const val RESTART_MESSAGE_GER =
        "Um Änderungen zu übernehmen, müssen Sie die Anwendung neu starten"

    const val RESTART_ENG = "Restart"
    const val RESTART_RUS = "Перезагрузить"
    const val RESTART_GER = "Starten Sie neu"

    const val CANCEL_ENG = "Cancel"
    const val CANCEL_RUS = "Отмена"
    const val CANCEL_GER = "Stornieren"

    const val GPS_ERROR_RUS =
        "Ошибка геолокации. Убедитесь, что локация включена на вашем устройстве"
    const val GPS_ERROR_ENG = "Geolocation error. Make sure the location is enabled on your device"
    const val GPS_ERROR_GER =
        "Geolokalisierungsfehler. Stellen Sie sicher, dass der Speicherort auf Ihrem Gerät aktiviert ist"

    val emptyInputErrorMessage: String
    get() = when (language) {

        RUS -> EMPTY_INPUT_ERROR_RUS
        ENG -> EMPTY_INPUT_ERROR_ENG
        GER -> EMPTY_INPUT_ERROR_GER

        }

    val locationDeniedError: String
        get() = when (language) {

            RUS -> LOCATION_DENIED_WARNING_RUS
            ENG -> LOCATION_DENIED_WARNING_ENG
            GER -> LOCATION_DENIED_WARNING_GER

        }

    val restartMessage: String
        get() = when (language) {

            RUS -> RESTART_MESSAGE_RUS
            ENG -> RESTART_MESSAGE_ENG
            else -> RESTART_MESSAGE_GER

        }

    val gpsErrorMessage: String
        get() = when (language) {

            RUS -> GPS_ERROR_RUS
            ENG -> GPS_ERROR_ENG
            else -> GPS_ERROR_GER

        }

    val cityIsAlreadyAttachedToNotificationsMessage: String
        get() = when (language) {

           RUS -> "Введённый город уже привязан к уведомлению"
           ENG -> "Entered city is already attached to notification"
            else -> "Die eingegebene Stadt ist bereits mit der Benachrichtigung verknüpft"

        }

    val notificationsWasResetSuccessfully: String
        get() = when (language) {

            RUS -> "Уведомления отменены"
            ENG -> "Notifications is reset"
            else -> "Benachrichtigungen abgebrochen"

        }

    val reboot: String
        get() = when (language) {

            RUS -> RESTART_RUS
            ENG -> RESTART_ENG
            else -> RESTART_GER

        }

    val successMessage: String
        get() = when (language) {

            RUS -> "Успешно"
            ENG -> "Successful"
            else -> "Erfolgreich"

        }

    val cancel: String
        get() = when (language) {

            RUS -> CANCEL_RUS
            ENG -> CANCEL_ENG
            GER -> CANCEL_GER

        }

    val locationTitle: String
        get() = when (language) {

            RUS -> YOUR_LOCATION_RUS
            ENG -> YOUR_LOCATION_ENG
            GER -> YOUR_LOCATION_GER

        }

    val cityNotFoundDesc: String
        get() = when (language) {

            RUS -> CITY_NOT_FOUND_RUS
            ENG -> CITY_NOT_FOUND_ENG
            GER -> CITY_NOT_FOUND_GER

        }

    val hour: String
        get() = when (language) {

            RUS -> "часа"
            ENG -> "hours"
            GER -> "Stunden"

        }

    val windSpeed: String
        get() = when (language) {

            RUS -> "Скорость ветра"
            ENG -> "Wind speed"
            GER -> "Windgeschwindigkeit"

        }

    val direction: String
        get() = when (language) {

            RUS -> "направление"
            ENG -> "direction"
            GER -> "richtung"

        }

    val humidity: String
        get() = when (language) {

            RUS -> "Влажность"
            ENG -> "Humidity"
            GER -> "Feuchtigkeit"

        }
}