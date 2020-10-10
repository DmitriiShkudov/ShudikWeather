package com.example.shkudikweatherapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.WeatherState
import com.example.shkudikweatherapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.lang.Exception

class SettingsActivity : AppCompatActivity() {

    private val checkedChangeListener =

        CompoundButton.OnCheckedChangeListener { rb, isChecked ->

            UserPreferences.degreeUnit =
                if (rb_c_unit.isChecked) UserPreferences.DegreeUnit.DEG_C else UserPreferences.DegreeUnit.DEG_F

        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsBackground.setImageDrawable(when (WeatherProvider.weatherState) {

            WeatherState.CLEAR -> ResourcesCompat.getDrawable(resources, R.drawable.clear, null)
            WeatherState.CLOUDY -> ResourcesCompat.getDrawable(resources, R.drawable.cloud, null)
            WeatherState.LOW_CLOUDY -> ResourcesCompat.getDrawable(resources, R.drawable.low_cloud, null)
            WeatherState.RAIN -> ResourcesCompat.getDrawable(resources, R.drawable.rain, null)
            WeatherState.HUMID -> ResourcesCompat.getDrawable(resources, R.drawable.humid, null)
            WeatherState.SNOW -> ResourcesCompat.getDrawable(resources, R.drawable.snow, null)
            WeatherState.LOW_SNOW -> ResourcesCompat.getDrawable(resources, R.drawable.low_snow, null)


        })

        // init
        setLocale()

        UserPreferences.apply {

            Log.d("LANG--->", this.language.str)

            imgLang.setImageDrawable(when (this.language) {

                UserPreferences.Language.GER -> ResourcesCompat.getDrawable(resources, R.drawable.germany, null)
                UserPreferences.Language.ENG -> ResourcesCompat.getDrawable(resources, R.drawable.england, null)
                else -> ResourcesCompat.getDrawable(resources, R.drawable.russia, null)

            })

            cbFullscreen.isChecked = this.fullscreen

            when (this.degreeUnit) {

                UserPreferences.DegreeUnit.DEG_C ->
                    rb_c_unit.also { it.setOnCheckedChangeListener(checkedChangeListener) }.isChecked =
                        true

                UserPreferences.DegreeUnit.DEG_F ->
                    rb_f_unit.also { it.setOnCheckedChangeListener(checkedChangeListener) }.isChecked =
                        true

            }

        }

        //
        btn_set_notifications.setOnClickListener {

            layoutSetNotifications.visibility = View.VISIBLE
            btn_apply_notifications.visibility = View.VISIBLE

        }

        spinnerLang.adapter = ArrayAdapter(applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            listOf(UserPreferences.Language.ENG.str, UserPreferences.Language.RUS.str, UserPreferences.Language.GER.str))

        var i = 0 // spinner bug

        spinnerLang.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                if (++i > 1) {

                    when (position) {

                        0 -> {

                            imgLang.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.england,
                                    null
                                )
                            )
                            UserPreferences.language = UserPreferences.Language.ENG
                        }
                        1 -> {
                            imgLang.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.russia,
                                    null
                                )
                            )
                            UserPreferences.language = UserPreferences.Language.RUS
                        }
                        2 -> {
                            imgLang.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.germany,
                                    null
                                )
                            )
                            UserPreferences.language = UserPreferences.Language.GER
                        }
                    }

                    setLocale()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        val hour = when (UserPreferences.language) {

                UserPreferences.Language.RUS -> "часа"
                UserPreferences.Language.ENG -> "hours"
                UserPreferences.Language.GER -> "Stunden"

            }

        spinnerInterval.adapter = ArrayAdapter(applicationContext,
            R.layout.tv_spinner, listOf("2 $hour", "4 $hour", "8 $hour", "12 $hour", "24 $hour"))

        spinnerInterval.setSelection(when (UserPreferences.notifInterval) {

            2 -> 0
            4 -> 1
            8 -> 2
            12 -> 3
            24 -> 4

            else -> throw Exception("The interval was set wrong")
        })

        input_notification_city.setText(UserPreferences.notifCity)

        btn_apply_notifications.setOnClickListener {}

        cbFullscreen.setOnCheckedChangeListener { compoundButton, b -> UserPreferences.fullscreen = b }

    }

    ///
    ///
    ///
    ///
    fun setLocaleText(obj: View) {

        when (UserPreferences.language) {

            UserPreferences.Language.RUS -> when (obj) {

                settings_header -> settings_header.text = "Настройки"
                text_fullscreen -> text_fullscreen.text = "Полный экран"
                text_language -> text_language.text = "Язык"
                text_push_notif -> text_push_notif.text = "Пуш-уведомления"
                btn_set_notifications -> btn_set_notifications.also { it.textSize = 12f }.text = "Установить"
                text_temp_unit -> text_temp_unit.also { it.textSize = 18f }.text = "Единица измерения температуры"
                input_notification_city -> input_notification_city.also { it.textSize = 15f }.hint = "Введите город"
                text_select_interval -> text_select_interval.also { it.textSize = 14f }.text = "Выберите интервал"
                btn_apply_notifications -> btn_apply_notifications.also { it.textSize = 12f }.text = "Применить"

            }

            UserPreferences.Language.GER -> when (obj) {

                settings_header -> settings_header.text = "Einstellungen"
                text_fullscreen -> text_fullscreen.text = "Vollbildschirm"
                text_language -> text_language.text = "Sprache"
                text_push_notif -> text_push_notif.text = "Mitteilungen"
                btn_set_notifications -> btn_set_notifications.also { it.textSize = 10f }.text = "Konfiguration"
                text_temp_unit -> text_temp_unit.text = "Temperatureinheit"
                input_notification_city -> input_notification_city.also { it.textSize = 16f }.hint = "Stadt betreten"
                text_select_interval -> text_select_interval.also { it.textSize = 13f }.text = "Wählen Sie das Intervall"
                btn_apply_notifications -> btn_apply_notifications.text = "Anwenden"

            }

            UserPreferences.Language.ENG -> when (obj) {

                settings_header -> settings_header.text = "Settings"
                text_fullscreen -> text_fullscreen.text = "Fullscreen"
                text_language -> text_language.text = "Language"
                text_push_notif -> text_push_notif.text = "Push-notifications"
                btn_set_notifications -> btn_set_notifications.also { it.textSize = 16f }.text = "Set"
                text_temp_unit -> text_temp_unit.text = "Temperature unit"
                input_notification_city -> input_notification_city.hint = "Enter city"
                text_select_interval -> text_select_interval.also { it.textSize = 15f }.text = "Select the interval"
                btn_apply_notifications -> btn_apply_notifications.text = "Apply"

            }

        }
    }

    fun setLocale() {

        setLocaleText(settings_header)
        setLocaleText(text_fullscreen)
        setLocaleText(text_language)
        setLocaleText(text_push_notif)
        setLocaleText(btn_set_notifications)
        setLocaleText(text_temp_unit)
        setLocaleText(input_notification_city)
        setLocaleText(btn_apply_notifications)
        setLocaleText(text_select_interval)


        val hour = when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "часа"
            UserPreferences.Language.ENG -> "hours"
            UserPreferences.Language.GER -> "Stunden"

        }

        spinnerInterval.adapter = ArrayAdapter(applicationContext,
            R.layout.tv_spinner, listOf("2 $hour", "4 $hour", "8 $hour", "12 $hour", "24 $hour"))
        
    }

}