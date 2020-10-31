package com.example.shkudikweatherapp.presentation_layer.settings_activity.states

import com.example.shkudikweatherapp.presentation_layer.common_protocols.State
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import com.example.shkudikweatherapp.presentation_layer.settings_activity.states.SettingsStates.CHANGING_NOTIFICATION_CITY
import com.example.shkudikweatherapp.presentation_layer.settings_activity.states.SettingsStates.UPDATED

class SettingsStateImpl(private val activity: SettingsActivity): State {
    override fun <SettingsStates> setState(state: SettingsStates) {

        when (state) {

            CHANGING_NOTIFICATION_CITY -> {}
            UPDATED -> {}

        }

    }
}