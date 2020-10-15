package com.example.shkudikweatherapp.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.MainDescription
import com.example.shkudikweatherapp.states.WeatherState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    companion object {

        lateinit var mainDesc: MainDescription

    }

    private val checkedChangeListener =

        CompoundButton.OnCheckedChangeListener { rb, isChecked ->

            UserPreferences.degreeUnit =
                if (rb_c_unit.isChecked) UserPreferences.DegreeUnit.DEG_C else UserPreferences.DegreeUnit.DEG_F

        }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (UserPreferences.fullscreen) setTheme(R.style.fullscreen)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsBackground.setImageDrawable(

            when (mainDesc) {

                MainDescription.CLEAR -> {

                    dayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.clear, null)

                }

                MainDescription.CLEAR_NIGHT -> {

                    nightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.clear_night, null)

                }

                MainDescription.CLOUDS -> {

                    dayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.low_cloud, null)

                }

                MainDescription.CLOUDS_NIGHT -> {

                    nightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.low_cloud_night, null)

                }

                MainDescription.RAIN -> {

                    dayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.rain, null)

                }

                MainDescription.RAIN_NIGHT -> {

                    nightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.rain_night, null)
                }

                MainDescription.HAZE, MainDescription.MIST, MainDescription.DUST,
                MainDescription.FOG, MainDescription.SMOKE -> {

                    dayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.humid, null)

                }

                MainDescription.HAZE_NIGHT, MainDescription.MIST_NIGHT, MainDescription.DUST_NIGHT,
                MainDescription.FOG_NIGHT, MainDescription.SMOKE_NIGHT -> {

                    nightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)

                }

                MainDescription.SNOW -> {

                    dayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.snow, null)

                }

                MainDescription.SNOW_NIGHT -> {

                    nightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.snow_night, null)

                }

                else -> {

                    nightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)

                }

            }
        )

        // init
        setLocale()

        UserPreferences.apply {

            imgLang.setImageDrawable(
                when (this.language) {

                    UserPreferences.Language.GER -> ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.germany,
                        null
                    )
                    UserPreferences.Language.ENG -> ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.england,
                        null
                    )
                    else -> ResourcesCompat.getDrawable(resources, R.drawable.russia, null)

                }
            )

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

        btn_apply_notifications.setOnClickListener {}

        cbFullscreen.setOnClickListener { view: View ->
            val builder = AlertDialog.Builder(this@SettingsActivity)
            val alertDialog: AlertDialog
            //
            val dialogOnPositiveClickListener: DialogInterface.OnClickListener =
                DialogInterface.OnClickListener { dialog, which ->

                    UserPreferences.fullscreen = cbFullscreen.isChecked
                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
                        addFlags(FLAG_ACTIVITY_NEW_TASK);
                    }

                    startActivity(intent)
                }
            val dialogOnNegativeClickListener: DialogInterface.OnClickListener =
                DialogInterface.OnClickListener { dialog, which ->

                    cbFullscreen.isChecked = UserPreferences.fullscreen
                    //returning
                }
            builder.setMessage("Чтобы применить изменения, необходимо перезагрузить приложение")
                .setCancelable(false)
                .setPositiveButton("Перезагрузить", dialogOnPositiveClickListener)
                .setNegativeButton("Отменить", dialogOnNegativeClickListener)
            //
            alertDialog = builder.create()
            alertDialog.show()
        }

    }


    fun nightMode() {

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

    fun dayMode() {

        imgLang.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.language, null))
        imgFullscreen.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.fullscreen, null))
        imgNotification.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.notifications, null))
        imgTempUnit.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.temp_unit, null))

        ResourcesCompat.getColor(resources, R.color.settingsTextColor, null).also {

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
                btn_set_notifications -> btn_set_notifications.also { it.textSize = 12f }.text =
                    "Установить"
                text_temp_unit -> text_temp_unit.also { it.textSize = 18f }.text =
                    "Единица измерения температуры"
                input_notification_city -> input_notification_city.also { it.textSize = 15f }.hint =
                    "Введите город"
                text_select_interval -> text_select_interval.also { it.textSize = 14f }.text =
                    "Выберите интервал"
                btn_apply_notifications -> btn_apply_notifications.also { it.textSize = 12f }.text =
                    "Применить"

            }

            UserPreferences.Language.GER -> when (obj) {

                settings_header -> settings_header.text = "Einstellungen"
                text_fullscreen -> text_fullscreen.text = "Vollbildschirm"
                text_language -> text_language.text = "Sprache"
                text_push_notif -> text_push_notif.text = "Mitteilungen"
                btn_set_notifications -> btn_set_notifications.also { it.textSize = 10f }.text =
                    "Konfiguration"
                text_temp_unit -> text_temp_unit.also { it.textSize = 19f }.text = "Temperatureinheit"
                input_notification_city -> input_notification_city.also { it.textSize = 16f }.hint =
                    "Stadt betreten"
                text_select_interval -> text_select_interval.also { it.textSize = 13f }.text =
                    "Wählen Sie das Intervall"
                btn_apply_notifications -> btn_apply_notifications.text = "Anwenden"

            }

            UserPreferences.Language.ENG -> when (obj) {

                settings_header -> settings_header.text = "Settings"
                text_fullscreen -> text_fullscreen.text = "Fullscreen"
                text_language -> text_language.text = "Language"
                text_push_notif -> text_push_notif.text = "Push-notifications"
                btn_set_notifications -> btn_set_notifications.also { it.textSize = 14f }.text =
                    "Set"
                text_temp_unit -> text_temp_unit.also { it.textSize = 19f }.text = "Temperature unit"
                input_notification_city -> input_notification_city.also { it.textSize = 18f }.hint = "Enter city"
                text_select_interval -> text_select_interval.also { it.textSize = 15f }.text =
                    "Select the interval"
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

        spinnerInterval.adapter = ArrayAdapter(
            applicationContext,
            R.layout.tv_spinner, listOf("2 $hour", "4 $hour", "8 $hour", "12 $hour", "24 $hour")
        )
        
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