package com.example.shkudikweatherapp.presentation_layer.settings_activity.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper.hour
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.presentation_layer.settings_activity.views.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var fullscreenImpl: FullscreenImpl
    private lateinit var temperatureUnitImpl: TemperatureUnitImpl
    private lateinit var backgroundImpl: BackgroundImpl
    private lateinit var languageImpl: LanguageImpl
    private lateinit var localeImpl: LocaleImpl
    private lateinit var notificationImpl: NotificationImpl
    private lateinit var timeModeImpl: TimeModeImpl

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

        localeImpl.setLocale()
        backgroundImpl.setBackground(mainDesc)

        UserPreferences.apply {

            languageImpl.setImgLanguage(this.language)
            fullscreenImpl.setFullscreenMode(this.fullscreen)
            temperatureUnitImpl.setTemperatureUnit(this.degreeUnit)

        }


        btn_set_notifications.setOnClickListener {

            notificationImpl.showNotificationOption()

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

        spinnerInterval.adapter = ArrayAdapter(
            applicationContext,
            R.layout.tv_spinner, listOf("2 $hour", "4 $hour", "8 $hour", "12 $hour", "24 $hour")
        )

        spinnerInterval.setSelection(
            when (UserPreferences.notifInterval) {

                2 -> 0
                4 -> 1
                8 -> 2
                12 -> 3
                24 -> 4

                else -> throw Exception("The interval was set wrong")
            }
        )

        input_notification_city.setText(UserPreferences.notifCity)


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

    fun applyNotifications() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nChannel = NotificationChannel("m", "m", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(nChannel)

        }

        btn_apply_notifications.setOnClickListener {

            val nBuilder = NotificationCompat.Builder(this@SettingsActivity, "m").
            setSmallIcon(R.drawable.settings).
            setContentTitle("Keulor Vamos").
            setContentText("Districtskoye - vamosskoye").
            setAutoCancel(true).
            setWhen(Calendar.getInstance().timeInMillis - 3000)

            val nManager = NotificationManagerCompat.from(this@SettingsActivity)

            nManager.notify(1, nBuilder.build())
        }
    }
}