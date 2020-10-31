package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
import android.widget.Toast
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface Notification {

    fun showNotificationOption()
    fun notificationSet()
    fun notificationReset()
    fun showError(errorMessage: String)

}

class NotificationImpl(private val activity: SettingsActivity) : Notification {

    override fun showNotificationOption() { with(activity) {

            layoutSetNotifications.visibility = View.VISIBLE

        }
    }

    override fun notificationSet() =
        Toast.makeText(activity, "Successfully", Toast.LENGTH_SHORT).show()

    override fun showError(errorMessage: String) =
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()

    override fun notificationReset() =
        Toast.makeText(activity, "Notifications was successfully reset", Toast.LENGTH_LONG).show()

}