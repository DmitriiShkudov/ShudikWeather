package com.example.shkudikweatherapp.presentation_layer.settings_activity.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shkudikweatherapp.data_layer.http_client.NotificationServiceRetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class SettingsViewModel(app: Application) : AndroidViewModel(app)  {

    suspend fun testLoad(city: String)
            = CoroutineScope(Dispatchers.IO).async { NotificationServiceRetrofitClient().loadWeather(city) }.await()

}