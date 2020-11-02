package com.example.shkudikweatherapp.presentation_layer.settings_activity.states

import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY
import com.example.shkudikweatherapp.data_layer.providers.Helper.hideKeyboard
import com.example.shkudikweatherapp.data_layer.providers.Helper.showKeyboard
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.notificationCity
import com.example.shkudikweatherapp.presentation_layer.common_protocols.State
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import com.example.shkudikweatherapp.presentation_layer.settings_activity.states.SettingsStates.CHANGING_NOTIFICATION_CITY
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsStateImpl(private val activity: SettingsActivity): State {

    override fun <SettingsStates> setState(state: SettingsStates) { with(activity) {

            when (state) {

                CHANGING_NOTIFICATION_CITY -> {

                    input_notification_city.setText(EMPTY)
                    input_notification_city.showKeyboard(activity)
                    input_notification_city.requestFocus()

                }
                else -> {

                    input_notification_city.setText(notificationCity)
                    input_notification_city.hideKeyboard(activity)
                    input_notification_city.clearFocus()

                }
            }
        }
    }
}