package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
import android.widget.ArrayAdapter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper.hour
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Locale
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

class LocaleImpl(private val activity: SettingsActivity) : Locale {

    override fun setLocale() { with(activity) {

            fun setLocaleText(obj: View) {

                when (UserPreferences.language) {

                    UserPreferences.Language.RUS -> when (obj) {

                        settings_header -> settings_header.text = "Настройки"
                        text_fullscreen -> text_fullscreen.text = "Полный экран"
                        text_language -> text_language.text = "Язык"
                        text_push_notif -> text_push_notif.text = "Пуш-уведомления"
                        btn_set_notifications -> btn_set_notifications.also { it.textSize = 12f }.text =
                            "Установить"
                        text_temp_unit -> text_temp_unit.also { it.textSize = 18f }.text =
                            "Единица измерения температуры"
                        input_notification_city -> input_notification_city.also {
                            it.textSize = 15f
                        }.hint =
                            "Введите город"
                        text_select_interval -> text_select_interval.also { it.textSize = 14f }.text =
                            "Выберите интервал"
                        btn_apply_notifications -> btn_apply_notifications.also {
                            it.textSize = 12f
                        }.text =
                            "Применить"

                    }

                    UserPreferences.Language.GER -> when (obj) {

                        settings_header -> settings_header.text = "Einstellungen"
                        text_fullscreen -> text_fullscreen.text = "Vollbildschirm"
                        text_language -> text_language.text = "Sprache"
                        text_push_notif -> text_push_notif.text = "Mitteilungen"
                        btn_set_notifications -> btn_set_notifications.also { it.textSize = 10f }.text =
                            "Konfiguration"
                        text_temp_unit -> text_temp_unit.also { it.textSize = 19f }.text =
                            "Temperatureinheit"
                        input_notification_city -> input_notification_city.also {
                            it.textSize = 16f
                        }.hint =
                            "Stadt betreten"
                        text_select_interval -> text_select_interval.also { it.textSize = 13f }.text =
                            "Wählen Sie das Intervall"
                        btn_apply_notifications -> btn_apply_notifications.text = "Anwenden"

                    }

                    UserPreferences.Language.ENG -> when (obj) {

                        settings_header -> settings_header.text = "Settings"
                        text_fullscreen -> text_fullscreen.text = "Fullscreen"
                        text_language -> text_language.text = "Language"
                        text_push_notif -> text_push_notif.text = "Push-notifications"
                        btn_set_notifications -> btn_set_notifications.also { it.textSize = 14f }.text =
                            "Set"
                        text_temp_unit -> text_temp_unit.also { it.textSize = 19f }.text =
                            "Temperature unit"
                        input_notification_city -> input_notification_city.also {
                            it.textSize = 18f
                        }.hint = "Enter city"
                        text_select_interval -> text_select_interval.also { it.textSize = 15f }.text =
                            "Select the interval"
                        btn_apply_notifications -> btn_apply_notifications.text = "Apply"

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
            setLocaleText(text_select_interval)

            spinnerInterval.adapter = ArrayAdapter(
                applicationContext,
                R.layout.tv_spinner, listOf("2 $hour", "4 $hour", "8 $hour", "12 $hour", "24 $hour")
            )

        }
    }

}