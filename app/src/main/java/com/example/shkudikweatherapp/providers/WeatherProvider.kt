package com.example.shkudikweatherapp.providers

import android.content.Context
import android.util.Log
import java.text.FieldPosition

object WeatherProvider {

    private const val CITIES_PREF = "Countries preferences"
    private const val SELECTED_CITY_NAME_KEY = "Selected country name"
    private const val DEFAULT_CITY_NAME = "Paris"
    private const val HELP_LIST_PREF = "Help list pref"
    private const val HELP_KEY = "Help_"
    private const val MAX_HELP_CITIES = 3


    enum class WeatherDesc {

        CLEAR, RAIN, HUMID, LOW_CLOUD, CLOUD, LOW_SNOW, SNOW

    }


    var context: Context? = null

    var selectedCity: String

    get() = this.prefCities?.
                getString(SELECTED_CITY_NAME_KEY, DEFAULT_CITY_NAME) ?: DEFAULT_CITY_NAME

    set(value) =
        this.prefCities?.edit()?.putString(SELECTED_CITY_NAME_KEY, value)!!.apply()

    var temperature: Int = 0
    var wind: Int = 0
    var humidity: Int = 0
    var description: String = String()


    var weatherDesc: WeatherDesc? = null

    get() = when (this.description) {

        "light shower snow" -> WeatherDesc.LOW_SNOW
        "clear sky" -> WeatherDesc.CLEAR
        "shower rain" -> WeatherDesc.RAIN
        "light rain" -> WeatherDesc.RAIN
        "rain" -> WeatherDesc.RAIN
        "moderate rain" -> WeatherDesc.RAIN
        "few clouds" -> WeatherDesc.LOW_CLOUD
        "broken clouds" -> WeatherDesc.LOW_CLOUD
        "scattered clouds" -> WeatherDesc.LOW_CLOUD
        "haze" -> WeatherDesc.HUMID
        "fog" -> WeatherDesc.HUMID
        else -> WeatherDesc.CLOUD

    }

    val helpList: ArrayList<String>
    get() {

        val arrayList = ArrayList<String>()

        arrayOf(this.prefHelp?.getString(HELP_KEY + "1", "") ?: "",
                this.prefHelp?.getString(HELP_KEY + "2", "") ?: "",
                this.prefHelp?.getString(HELP_KEY + "3", "") ?: "")
            .forEach {

                if (it.isNotEmpty()) {

                    arrayList.add(it)

                }

        }

        return arrayList

    }

    private val prefCities
    get() = this.context?.getSharedPreferences(CITIES_PREF, 0)

    private val prefHelp
    get() = this.context?.getSharedPreferences(HELP_LIST_PREF, 0)

    fun addHelpCity(helpCity: String) {

        if (this.helpList.isNotEmpty())
            if (this.helpList.last() == helpCity) return

        when (this.helpList.size) {


            0 -> this.prefHelp?.edit()?.putString(HELP_KEY + "1", helpCity)?.apply()
            1 -> this.prefHelp?.edit()?.putString(HELP_KEY + "2", helpCity)?.apply()
            2 -> this.prefHelp?.edit()?.putString(HELP_KEY + "3", helpCity)?.apply()
            else -> {

                this.prefHelp?.edit()?.putString(HELP_KEY + "1",
                    this.prefHelp?.getString(HELP_KEY + "2", ""))?.apply()

                this.prefHelp?.edit()?.putString(HELP_KEY + "2",
                    this.prefHelp?.getString(HELP_KEY + "3", ""))?.apply()

                this.prefHelp?.edit()?.putString(HELP_KEY + "3", helpCity)?.apply()

            }

        }
    }

    fun removeHelpCity(position: Int) {

        val posInPref = position + 1

        Log.d("POS", position.toString())

        this.prefHelp?.edit()?.putString(HELP_KEY + (position + 1).toString(), "")?.apply()

        for (i in posInPref + 1..MAX_HELP_CITIES + 1) {

            this.prefHelp?.edit()?.
                putString(HELP_KEY + (i - 1), this.prefHelp?.getString(HELP_KEY + i, ""))?.apply()

        }

    }

}