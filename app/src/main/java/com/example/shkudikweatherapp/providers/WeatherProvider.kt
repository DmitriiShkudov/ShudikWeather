package com.example.shkudikweatherapp.providers

import android.content.Context
import android.util.Log
import com.example.shkudikweatherapp.helpers.Helper
import com.example.shkudikweatherapp.states.WeatherState
import java.text.FieldPosition

object WeatherProvider {

    private const val CITIES_PREF = "Countries preferences"
    private const val SELECTED_CITY_NAME_KEY = "Selected country name"
    private const val DEFAULT_CITY_NAME = "Paris"
    private const val HELP_LIST_PREF = "Help list pref"
    private const val HELP_KEY = "Help_"
    private const val EMPTY = ""
    private const val MAX_HELP_CITIES = 3

    var context: Context? = null

    var selectedCity: String

    get() = this.prefCities?.
                getString(SELECTED_CITY_NAME_KEY, DEFAULT_CITY_NAME) ?: DEFAULT_CITY_NAME

    set(value) =
        this.prefCities?.edit()?.putString(SELECTED_CITY_NAME_KEY, value.also { addHelpCity(it) })!!.apply()

    var description: String = String()
    val weatherState: WeatherState
    get() = Helper.funnyDescDetermination(this.description)

    val helpList: ArrayList<String>
    get() {

        val arrayList = ArrayList<String>()

        arrayOf(this.prefHelp?.getString(HELP_KEY + "1", EMPTY) ?: EMPTY,
                this.prefHelp?.getString(HELP_KEY + "2", EMPTY) ?: EMPTY,
                this.prefHelp?.getString(HELP_KEY + "3", EMPTY) ?: EMPTY)
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
                    this.prefHelp?.getString(HELP_KEY + "2", EMPTY))?.apply()

                this.prefHelp?.edit()?.putString(HELP_KEY + "2",
                    this.prefHelp?.getString(HELP_KEY + "3", EMPTY))?.apply()

                this.prefHelp?.edit()?.putString(HELP_KEY + "3", helpCity)?.apply()

            }

        }
    }

    fun removeHelpCity(position: Int) {

        val posInPref = position + 1

        this.prefHelp?.edit()?.putString(HELP_KEY + (posInPref).toString(), EMPTY)?.apply()

        for (i in posInPref + 1..MAX_HELP_CITIES + 1) {

            this.prefHelp?.edit()?.
                putString(HELP_KEY + (i - 1), this.prefHelp?.getString(HELP_KEY + i, EMPTY))?.apply()

        }

    }

}