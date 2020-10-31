package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface Notification {

    fun showNotificationOption()
    fun notificationSet()

}

class NotificationImpl(private val activity: SettingsActivity) : Notification {

    override fun showNotificationOption() { with(activity) {

            layoutSetNotifications.visibility = View.VISIBLE

        }

    }

    override fun notificationSet() {

    }

}