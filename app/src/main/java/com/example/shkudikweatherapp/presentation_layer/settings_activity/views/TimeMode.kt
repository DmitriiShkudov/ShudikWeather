package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.presentation_layer.common_protocols.TimeMode
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

class TimeModeImpl(private val activity: SettingsActivity) : TimeMode {

    override fun setDayMode() { with(activity) {

            imgLang.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.language))

            imgFullscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fullscreen))

            imgNotification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.notifications))

            imgTempUnit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.temp_unit))

            ResourcesCompat.getColor(resources, R.color.black, null).also {

                settings_header.setTextColor(it)
                settings_header.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.settings_small,0,0,0)
                text_push_notif.setTextColor(it)
                text_temp_unit.setTextColor(it)
                text_language.setTextColor(it)
                text_fullscreen.setTextColor(it)
                rb_f_unit.setTextColor(it)
                rb_c_unit.setTextColor(it)
                input_notification_city.setTextColor(it)
                input_notification_city.setHintTextColor(it)

            }
        }
    }

    override fun setNightMode() { with(activity) {

            imgLanguage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.language_night))

            imgFullscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fullscreen_night))

            imgNotification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.notif_night))

            imgTempUnit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.temp_unit_night))

            ResourcesCompat.getColor(resources, R.color.white, null).also {


                settings_header.setTextColor(it)
                settings_header.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.settings_small_night,0,0,0)
                text_push_notif.setTextColor(it)
                text_temp_unit.setTextColor(it)
                text_language.setTextColor(it)
                text_fullscreen.setTextColor(it)
                rb_f_unit.setTextColor(it)
                rb_c_unit.setTextColor(it)
                input_notification_city.setTextColor(it)
                input_notification_city.setHintTextColor(it)


            }
        }
    }

    override fun setTimeMode(isNight: Boolean) =
        if (isNight || mainDesc == MainDescription.THUNDERSTORM) setNightMode() else setDayMode()

}