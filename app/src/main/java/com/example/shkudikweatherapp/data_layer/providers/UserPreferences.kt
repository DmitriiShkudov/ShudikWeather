package com.example.shkudikweatherapp.data_layer.providers

import android.content.Context
import com.example.shkudikweatherapp.data_layer.providers.Helper.DEGREE_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY
import com.example.shkudikweatherapp.data_layer.providers.Helper.FULLSCREEN_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.LANG_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.LOCATION_APPLIED_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.NOTIF_CITY_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.NOTIF_INTERVAL_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.SEARCH_MODE_KEY
import com.example.shkudikweatherapp.data_layer.providers.Helper.USER_PREF

object UserPreferences {

    enum class Language(val str: String, val apiStr: String) {
        RUS("Русский", "ru"), ENG("English","en"), GER("Deutsch", "de")
    }
    enum class TemperatureUnit(val str: String) { DEG_F(" °F"), DEG_C(" °C") }
    enum class SearchMode(val isCity: Boolean) { CITY(true), GEO(false) }

    var context: Context? = null

    val pref
        get() = this.context?.getSharedPreferences(USER_PREF, 0)

    var isLocationApplied: Boolean
        get() = this.pref?.getBoolean(LOCATION_APPLIED_KEY, false) ?: false
        set(value) = this.pref?.edit()?.putBoolean(LOCATION_APPLIED_KEY, value)?.apply()!!

    var searchMode: SearchMode
        get() = when (this.pref?.getBoolean(SEARCH_MODE_KEY, SearchMode.CITY.isCity) ?: SearchMode.CITY.isCity) {

            true -> SearchMode.CITY
            false -> SearchMode.GEO

        }
        set(value) = this.pref?.edit()?.putBoolean(SEARCH_MODE_KEY, value.isCity)?.apply()!!


    var language
        get() = when (this.pref?.getString(LANG_KEY, Language.ENG.str) ?: Language.ENG.str) {
            Language.RUS.str -> Language.RUS
            Language.ENG.str -> Language.ENG
            else -> Language.GER
        }
        set(value) = this.pref?.edit()?.putString(LANG_KEY, value.str)?.apply()!!

    var degreeUnit
        get() = when (this.pref?.getString(DEGREE_KEY, TemperatureUnit.DEG_C.str) ?: TemperatureUnit.DEG_C.str) {

            TemperatureUnit.DEG_C.str -> TemperatureUnit.DEG_C
            else -> TemperatureUnit.DEG_F

        }
        set(value) = this.pref?.edit()?.putString(DEGREE_KEY, value.str)?.apply()!!

    var fullscreen
        get() = this.pref?.getBoolean(FULLSCREEN_KEY, false) ?: false
        set(value) = this.pref?.edit()?.putBoolean(FULLSCREEN_KEY, value)?.apply()!!

}