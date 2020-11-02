package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
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

                        settings_header -> settings_header.text = "Настройки"
                        text_fullscreen -> text_fullscreen.text = "Полный экран"
                        text_language -> text_language.text = "Язык"
                        text_push_notif -> text_push_notif.text = "Пуш-уведомления"
                        btn_set_notifications -> btn_set_notifications.text = "Установить"
                        text_temp_unit -> text_temp_unit.text = "Единица измерения температуры"
                        input_notification_city -> input_notification_city.hint = "Введите город"
                        btn_apply_notifications -> btn_apply_notifications.text = "Применить"
                        btn_reset_notifications -> btn_reset_notifications.text = "Сбросить"

                    }

                    GER -> when (obj) {

                        settings_header -> settings_header.text = "Einstellungen"
                        text_fullscreen -> text_fullscreen.text = "Vollbildschirm"
                        text_language -> text_language.text = "Sprache"
                        text_push_notif -> text_push_notif.text = "Mitteilungen"
                        btn_set_notifications -> btn_set_notifications.text =
                            "Konfiguration"
                        text_temp_unit -> text_temp_unit.text =
                            "Temperatureinheit"
                        input_notification_city -> input_notification_city.hint =
                            "Stadt betreten"
                        btn_apply_notifications -> btn_apply_notifications.text = "Anwenden"
                        btn_reset_notifications -> btn_reset_notifications.text = "Zurücksetzen"

                    }

                    ENG -> when (obj) {

                        settings_header -> settings_header.text = "Settings"
                        text_fullscreen -> text_fullscreen.text = "Fullscreen"
                        text_language -> text_language.text = "Language"
                        text_push_notif -> text_push_notif.text = "Push-notifications"
                        btn_set_notifications -> btn_set_notifications.text = "Set"
                        text_temp_unit -> text_temp_unit.text = "Temperature unit"
                        input_notification_city -> input_notification_city.hint = "Enter city"
                        btn_apply_notifications -> btn_apply_notifications.text = "Apply"
                        btn_reset_notifications -> btn_reset_notifications.text = "Reset"

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