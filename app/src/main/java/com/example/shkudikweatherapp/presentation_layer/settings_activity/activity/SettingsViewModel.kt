package com.example.shkudikweatherapp.presentation_layer.settings_activity.activity

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.shkudikweatherapp.presentation_layer.notification.NotificationService
import com.example.shkudikweatherapp.data_layer.http_client.NotificationServiceRetrofitClient
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.cityIsAlreadyAttachedToNotificationsMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.cityNotExistsMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.emptyInputMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.reformat
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.notificationCity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class SettingsViewModel(app: Application) : AndroidViewModel(app)  {

    private suspend fun testLoad(city: String)
            = CoroutineScope(IO).async { NotificationServiceRetrofitClient().loadWeather(city) }.await()

    internal suspend fun applyNotification(activity: SettingsActivity) {

        coroutineScope { with(activity) {

                if (input_notification_city.text.isNotEmpty()) {

                    input_notification_city.reformat()
                    val enteredCity = input_notification_city.text.toString()

                    if (enteredCity != notificationCity) {

                        if (testLoad(enteredCity) != null) {

                            notificationCity = enteredCity
                            startService(Intent(applicationContext, NotificationService::class.java))

                            notificationImpl.notificationSet()

                        } else {

                            notificationImpl.showError(cityNotExistsMessage)

                        }

                    } else {

                        notificationImpl.showError(cityIsAlreadyAttachedToNotificationsMessage)

                    }

                } else {

                    notificationImpl.showError(emptyInputMessage)

                }
            }
        }
    }
}