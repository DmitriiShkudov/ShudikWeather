package com.example.shkudikweatherapp.data_layer.providers

import android.content.Context
import com.example.shkudikweatherapp.data_layer.providers.Helper.DEFAULT_CITY_NAME
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY
import com.example.shkudikweatherapp.data_layer.providers.Helper.HELP_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.MAX_HELP_CITIES
import com.example.shkudikweatherapp.data_layer.providers.Helper.NOTIFICATION_CITY_NAME_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.SELECTED_CITY_NAME_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.SELECTED_LAT_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.SELECTED_LON_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.WEATHER_PREF
import com.example.shkudikweatherapp.data_layer.enums.MainDescription

object WeatherProvider {

    var context: Context? = null

    private val pref
        get() = this.context?.getSharedPreferences(WEATHER_PREF, 0)

    var notificationCity: String?
        get() = this.pref?.getString(NOTIFICATION_CITY_NAME_KEY, null)
        set(value) = this.pref?.edit()?.putString(NOTIFICATION_CITY_NAME_KEY, value)?.apply()!!

    var selectedCity: String
        get() = this.pref?.getString(SELECTED_CITY_NAME_KEY, DEFAULT_CITY_NAME) ?: DEFAULT_CITY_NAME
        set(value) = this.pref?.edit()?.putString(SELECTED_CITY_NAME_KEY, value)?.apply()!!

    var selectedLat: Float
        get() = this.pref?.getFloat(SELECTED_LAT_KEY, 0f) ?: 0f
        set(value) = this.pref?.edit()?.putFloat(SELECTED_LAT_KEY, value)?.apply()!!

    var selectedLon: Float
        get() = this.pref?.getFloat(SELECTED_LON_KEY, 0f) ?: 0f
        set(value) = this.pref?.edit()?.putFloat(SELECTED_LON_KEY, value)?.apply()!!

    var isSelectedCityExists = true
    var isNight = false
    var mainDesc = MainDescription.CLEAR
    var desc = String()

    val helpList: ArrayList<String>
        get() {
            val arrayList = ArrayList<String>()
            arrayOf(this.pref?.getString(HELP_KEY + "1", EMPTY) ?: EMPTY,
                    this.pref?.getString(HELP_KEY + "2", EMPTY) ?: EMPTY,
                    this.pref?.getString(HELP_KEY + "3", EMPTY) ?: EMPTY)
                .forEach {

                    if (it.isNotEmpty()) {

                        arrayList.add(it)

                    }
            }
            return arrayList
        }

    fun addHelpCity(helpCity: String) {

        if (this.helpList.isNotEmpty())
            if (helpCity in this.helpList) return

        when (this.helpList.size) {

            0 -> this.pref?.edit()?.putString(HELP_KEY + "1", helpCity)?.apply()
            1 -> this.pref?.edit()?.putString(HELP_KEY + "2", helpCity)?.apply()
            2 -> this.pref?.edit()?.putString(HELP_KEY + "3", helpCity)?.apply()
            else -> {

                this.pref?.edit()?.putString(HELP_KEY + "1",
                    this.pref?.getString(HELP_KEY + "2", EMPTY))?.apply()

                this.pref?.edit()?.putString(HELP_KEY + "2",
                    this.pref?.getString(HELP_KEY + "3", EMPTY))?.apply()

                this.pref?.edit()?.putString(HELP_KEY + "3", helpCity)?.apply()

            }

        }
    }

    fun removeHelpCity(position: Int) {

        val posInPref = position + 1

        this.pref?.edit()?.putString(HELP_KEY + (posInPref).toString(), EMPTY)?.apply()

        for (i in posInPref + 1..MAX_HELP_CITIES + 1) {

            this.pref?.edit()?.
                putString(HELP_KEY + (i - 1), this.pref?.getString(HELP_KEY + i, EMPTY))?.apply()

        }

    }

}