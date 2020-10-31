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
import com.example.shkudikweatherapp.data_layer.providers.Helper.cityNotFoundDesc
import com.example.shkudikweatherapp.data_layer.providers.Helper.emptyInputErrorMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.hideKeyboard
import com.example.shkudikweatherapp.data_layer.providers.Helper.hour
import com.example.shkudikweatherapp.data_layer.providers.Helper.reformat
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.notificationCity
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.BoardImpl
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

    lateinit var fullscreenImpl: FullscreenImpl
        private set
    lateinit var temperatureUnitImpl: TemperatureUnitImpl
        private set
    lateinit var backgroundImpl: BackgroundImpl
        private set
    lateinit var languageImpl: LanguageImpl
        private set
    lateinit var localeImpl: LocaleImpl
        private set
    lateinit var notificationImpl: NotificationImpl
        private set
    lateinit var timeModeImpl: TimeModeImpl
        private set

    lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        //
        temperatureUnitImpl = TemperatureUnitImpl(this)
        notificationImpl = NotificationImpl(this)
        timeModeImpl = TimeModeImpl(this)
        fullscreenImpl = FullscreenImpl(this)
        languageImpl = LanguageImpl(this)
        localeImpl = LocaleImpl(this)
        backgroundImpl = BackgroundImpl(this, timeModeImpl)

        fullscreenImpl.setFullscreenMode(UserPreferences.fullscreen)

        //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        localeImpl.setLocale()
        backgroundImpl.setBackground(mainDesc)

        UserPreferences.apply {

            languageImpl.setImgLanguage(this.language)
            fullscreenImpl.setFullscreenMode(this.fullscreen)
            temperatureUnitImpl.setTemperatureUnit(this.degreeUnit)

        }

        input_notification_city.setOnEditorActionListener { textView, i, keyEvent ->

            if (i == 6) {

                CoroutineScope(Main).launch {

                    if (input_notification_city.text.isNotEmpty()) {

                        input_notification_city.reformat()
                        val enteredCity = input_notification_city.text.toString()

                        if (testLoad(enteredCity) != null) {

                            notificationCity = enteredCity
                            startService(Intent(applicationContext, NotificationService::class.java))
                            Toast.makeText(applicationContext, "Successfully", Toast.LENGTH_LONG).show()

                        } else {

                            Toast.makeText(applicationContext, cityNotFoundDesc, Toast.LENGTH_SHORT)
                                .show()

                        }

                    } else {

                        Toast.makeText(applicationContext, emptyInputErrorMessage, Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                input_notification_city.hideKeyboard(applicationContext)

            }
            true
        }

        btn_apply_notifications.setOnClickListener {

            CoroutineScope(Main).launch {

                if (input_notification_city.text.isNotEmpty()) {

                    input_notification_city.reformat()
                    val enteredCity = input_notification_city.text.toString()

                    if (testLoad(enteredCity) != null) {

                        notificationCity = enteredCity
                        startService(Intent(applicationContext, NotificationService::class.java))

                        notificationImpl.notificationSet()

                    } else {

                        notificationImpl.showError(cityNotFoundDesc)

                    }

                } else {

                    notificationImpl.showError(emptyInputErrorMessage)

                }
            }

            input_notification_city.clearFocus()
            input_notification_city.hideKeyboard(applicationContext)

        }

        input_notification_city.onFocusChangeListener =
            View.OnFocusChangeListener { p0, p1 ->
                if (p1) {
                    input_notification_city.text.clear()
                }
            }

        btn_set_notifications.setOnClickListener {

            notificationImpl.showNotificationOption()
            input_notification_city.setText(notificationCity)

        }

        btn_reset_notifications.setOnClickListener {

            stopService(Intent(applicationContext, NotificationService::class.java))
            notificationImpl.notificationReset()

        }

        spinnerLang.adapter = ArrayAdapter(
            applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            listOf(
                UserPreferences.Language.ENG.str,
                UserPreferences.Language.RUS.str,
                UserPreferences.Language.GER.str
            )
        )

        var i = 0 // spinner bug

        spinnerLang.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                if (++i > 1) {

                    when (position) {

                        0 -> languageImpl.setLanguage(UserPreferences.Language.ENG)
                        1 -> languageImpl.setLanguage(UserPreferences.Language.RUS)
                        2 -> languageImpl.setLanguage(UserPreferences.Language.GER)
                    }

                    localeImpl.setLocale()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        cbFullscreen.setOnClickListener {

            fullscreenImpl.setChecked(cbFullscreen.isChecked)

        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        spinnerLang.setSelection(when (UserPreferences.language) {

            UserPreferences.Language.ENG -> 0
            UserPreferences.Language.RUS -> 1
            UserPreferences.Language.GER -> 2

        })
    }

}