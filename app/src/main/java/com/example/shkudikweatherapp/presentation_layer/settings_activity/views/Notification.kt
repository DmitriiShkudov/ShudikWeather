package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
import android.widget.Toast
import com.example.shkudikweatherapp.data_layer.providers.Helper.notificationsWasResetSuccessfully
import com.example.shkudikweatherapp.data_layer.providers.Helper.successMessage
import com.example.shkudikweatherapp.presentation_layer.common_protocols.IError
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface Notification: IError {

    fun showNotificationOption()
    fun notificationSet()
    fun notificationReset()

}

class NotificationImpl(private val activity: SettingsActivity) : Notification {

    override fun showNotificationOption() { with(activity) {

            layoutSetNotifications.visibility = View.VISIBLE

        }
    }

    override fun notificationSet() =
        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show()

    override fun showError(message: String) =
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()

    override fun notificationReset() =
        Toast.makeText(activity, notificationsWasResetSuccessfully, Toast.LENGTH_SHORT).show()

}