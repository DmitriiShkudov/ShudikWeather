package com.example.shkudikweatherapp.presentation_layer.settings_activity.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.shkudikweatherapp.NotificationService
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.http_client.NotificationServiceRetrofitClient
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY
import com.example.shkudikweatherapp.data_layer.providers.Helper.KEY_BOARD_CODE_6
import com.example.shkudikweatherapp.data_layer.providers.Helper.cityNotFoundDesc
import com.example.shkudikweatherapp.data_layer.providers.Helper.emptyInputErrorMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.hideKeyboard
import com.example.shkudikweatherapp.data_layer.providers.Helper.hour
import com.example.shkudikweatherapp.data_layer.providers.Helper.reformat
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.fullscreen
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isNight
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.notificationCity
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.BoardImpl
import com.example.shkudikweatherapp.presentation_layer.settings_activity.states.SettingsStateImpl
import com.example.shkudikweatherapp.presentation_layer.settings_activity.states.SettingsStates
import com.example.shkudikweatherapp.presentation_layer.settings_activity.views.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    internal val temperatureUnitImpl = TemperatureUnitImpl(this)
    internal val notificationImpl = NotificationImpl(this)
    internal val timeModeImpl = TimeModeImpl(this)
    internal val fullscreenImpl = FullscreenImpl(this)
    internal val languageImpl = LanguageImpl(this)
    internal val localeImpl = LocaleImpl(this)
    internal val backgroundImpl = BackgroundImpl(this)
    internal val spinnerLanguageImpl = SpinnerLanguageImpl(this)
    internal val stateImpl = SettingsStateImpl(this)

    lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        fullscreenImpl.setFullscreenMode(fullscreen)

        //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        localeImpl.setLocale()
        backgroundImpl.setBackground(mainDesc)
        spinnerLanguageImpl.init()
        timeModeImpl.setTimeMode(isNight)

        UserPreferences.apply {

            languageImpl.setImgLanguage(this.language)
            fullscreenImpl.setFullscreenMode(this.fullscreen)
            temperatureUnitImpl.setTemperatureUnit(this.degreeUnit)

        }

        input_notification_city.setOnEditorActionListener { textView, i, keyEvent ->

            if (i == KEY_BOARD_CODE_6) {

                CoroutineScope(Main).launch {

                    viewModel.applyNotification(this@SettingsActivity)

                }.invokeOnCompletion {

                    stateImpl.setState(SettingsStates.UPDATED)

                }
            }
            true
        }

        btn_apply_notifications.setOnClickListener {

            CoroutineScope(Main).launch {

                viewModel.applyNotification(this@SettingsActivity)

            }.invokeOnCompletion {

                stateImpl.setState(SettingsStates.UPDATED)

            }
        }

        input_notification_city.setOnClickListener {

            stateImpl.setState(SettingsStates.CHANGING_NOTIFICATION_CITY)

        }

        btn_set_notifications.setOnClickListener {

            notificationImpl.showNotificationOption()
            input_notification_city.setText(notificationCity)
            btn_reset_notifications.isClickable = notificationCity.isNotEmpty()

        }

        btn_reset_notifications.setOnClickListener {

            notificationImpl.notificationReset()
            notificationCity = EMPTY
            stateImpl.setState(SettingsStates.UPDATED)
            stopService(Intent(applicationContext, NotificationService::class.java))

        }

        cbFullscreen.setOnClickListener {

            fullscreenImpl.setChecked(cbFullscreen.isChecked)

        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        spinnerLanguageImpl.setLanguage(language)

    }
}