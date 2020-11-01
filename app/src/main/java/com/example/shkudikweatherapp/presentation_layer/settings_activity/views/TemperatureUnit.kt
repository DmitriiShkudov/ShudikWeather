package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.widget.CompoundButton
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.TemperatureUnit.*
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.degreeUnit
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface TemperatureUnit {

    fun setTemperatureUnit(temperatureUnit: UserPreferences.TemperatureUnit)
    fun setCelsiumUnit()
    fun setFahrenheitUnit()

}

class TemperatureUnitImpl(private val activity: SettingsActivity) : TemperatureUnit {

    private val checkedChangeListener =

        CompoundButton.OnCheckedChangeListener { rb, isChecked ->
            degreeUnit =
                if (activity.rb_c_unit.isChecked)
                    DEG_C
                else
                    DEG_F

        }

    override fun setTemperatureUnit(temperatureUnit: UserPreferences.TemperatureUnit) {

        when (temperatureUnit) {

            DEG_C -> setCelsiumUnit()
            DEG_F -> setFahrenheitUnit()

        }

    }

    override fun setCelsiumUnit() {
        activity.rb_c_unit.also { it.setOnCheckedChangeListener(checkedChangeListener) }.isChecked = true
    }

    override fun setFahrenheitUnit() {
        activity.rb_f_unit.also { it.setOnCheckedChangeListener(checkedChangeListener) }.isChecked = true
    }

}