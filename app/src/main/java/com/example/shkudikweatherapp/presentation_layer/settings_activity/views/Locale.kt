package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.Language.*
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Locale
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

class LocaleImpl(private val activity: SettingsActivity) : Locale {

    override fun setLocale() { with(activity) {

            fun setLocaleText(obj: View) {

                when (language) {

                    RUS -> when (obj) {

                        settings_header -> settings_header.text = getString(R.string.settings_rus)
                        text_fullscreen -> text_fullscreen.text = getString(R.string.fullscreen_rus)
                        text_language -> text_language.text = getString(R.string.language_rus)
                        text_push_notif -> text_push_notif.text = getString(R.string.notifications_rus)
                        btn_set_notifications -> btn_set_notifications.text = getString(R.string.set_btn_rus)
                        text_temp_unit -> text_temp_unit.text = getString(R.string.temperature_unit_rus)
                        input_notification_city -> input_notification_city.hint = getString(R.string.enter_city_hint_rus)
                        btn_apply_notifications -> btn_apply_notifications.text = getString(R.string.apply_btn_rus)
                        btn_reset_notifications -> btn_reset_notifications.text = getString(R.string.reset_btn_rus)

                    }

                    GER -> when (obj) {

                        settings_header -> settings_header.text = getString(R.string.settings_ger)
                        text_fullscreen -> text_fullscreen.text = getString(R.string.fullscreen_ger)
                        text_language -> text_language.text = getString(R.string.language_ger)
                        text_push_notif -> text_push_notif.text = getString(R.string.notifications_ger)
                        btn_set_notifications -> btn_set_notifications.text = getString(R.string.set_btn_ger)
                        text_temp_unit -> text_temp_unit.text = getString(R.string.temperature_unit_ger)
                        input_notification_city -> input_notification_city.hint = getString(R.string.enter_city_hint_ger)
                        btn_apply_notifications -> btn_apply_notifications.text = getString(R.string.apply_btn_ger)
                        btn_reset_notifications -> btn_reset_notifications.text = getString(R.string.reset_btn_ger)

                    }

                    ENG -> when (obj) {

                        settings_header -> settings_header.text = getString(R.string.settings_eng)
                        text_fullscreen -> text_fullscreen.text = getString(R.string.fullscreen_eng)
                        text_language -> text_language.text = getString(R.string.language_eng)
                        text_push_notif -> text_push_notif.text = getString(R.string.notifications_eng)
                        btn_set_notifications -> btn_set_notifications.text = getString(R.string.set_btn_eng)
                        text_temp_unit -> text_temp_unit.text = getString(R.string.temperature_unit_eng)
                        input_notification_city -> input_notification_city.hint = getString(R.string.enter_city_hint_eng)
                        btn_apply_notifications -> btn_apply_notifications.text = getString(R.string.apply_btn_eng)
                        btn_reset_notifications -> btn_reset_notifications.text = getString(R.string.reset_btn_eng)

                    }

                }
            }

            setLocaleText(settings_header)
            setLocaleText(text_fullscreen)
            setLocaleText(text_language)
            setLocaleText(text_push_notif)
            setLocaleText(btn_set_notifications)
            setLocaleText(text_temp_unit)
            setLocaleText(input_notification_city)
            setLocaleText(btn_apply_notifications)
            setLocaleText(btn_reset_notifications)

        }
    }
}