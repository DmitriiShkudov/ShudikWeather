package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.common_protocols.TimeMode
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

class TimeModeImpl(private val activity: SettingsActivity) : TimeMode {

    override fun setDayMode() { with(activity) {

            imgLang.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.language, null))

            imgFullscreen.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.fullscreen, null))

            imgNotification.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.notifications, null))

            imgTempUnit.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.temp_unit, null))

            ResourcesCompat.getColor(resources, R.color.black, null).also {

                settings_header.setTextColor(it)
                text_select_interval.setTextColor(it)
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
            imgLanguage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.language_night, null))

            imgFullscreen.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.fullscreen_night, null))

            imgNotification.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.notif_night, null))

            imgTempUnit.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.temp_unit_night, null))

            ResourcesCompat.getColor(resources, R.color.white, null).also {


                settings_header.setTextColor(it)
                text_select_interval.setTextColor(it)
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

    override fun setTimeMode(isNight: Boolean) = if (isNight) setNightMode() else setDayMode()


}