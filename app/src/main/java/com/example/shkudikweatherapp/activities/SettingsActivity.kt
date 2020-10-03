package com.example.shkudikweatherapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presenters.main_activity.BackgroundPresenter
import com.example.shkudikweatherapp.presenters.settings_activity.BtnApplyPresenter
import com.example.shkudikweatherapp.presenters.settings_activity.LanguageSettingsPresenter
import com.example.shkudikweatherapp.presenters.settings_activity.LanguageSettingsPresenter.Companion.ENG_LANG
import com.example.shkudikweatherapp.presenters.settings_activity.LanguageSettingsPresenter.Companion.GER_LANG
import com.example.shkudikweatherapp.presenters.settings_activity.LanguageSettingsPresenter.Companion.RUS_LANG
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.views.settings_activity.BtnApply
import com.example.shkudikweatherapp.views.settings_activity.LanguageSettings
import com.example.shkudikweatherapp.weather_states.Background
import kotlinx.android.synthetic.main.activity_settings.*
import java.lang.Exception

class SettingsActivity : MvpAppCompatActivity(), Background, LanguageSettings, BtnApply {

    @InjectPresenter
    lateinit var backgroundPresenter: BackgroundPresenter

    @InjectPresenter
    lateinit var languageSettingsPresenter: LanguageSettingsPresenter

    @InjectPresenter
    lateinit var btnApplyPresenter: BtnApplyPresenter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // init
        UserPreferences.context = applicationContext
        backgroundPresenter.set()
        languageSettingsPresenter.updateLang()
        cbFullscreen.isChecked = UserPreferences.fullscreen
        Log.d("FUL", UserPreferences.fullscreen.toString())

        //
        btnSetNotifications.setOnClickListener {

            layoutSetNotifications.visibility = View.VISIBLE
            btnApplyNotifications.visibility = View.VISIBLE

        }

        var spinnerShit = 0

        spinnerLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {

                if (++spinnerShit > 1) {

                    when (spinnerLang.selectedItem) {

                        RUS_LANG -> languageSettingsPresenter.changeLang(UserPreferences.Language.RUS)

                        ENG_LANG -> languageSettingsPresenter.changeLang(UserPreferences.Language.ENG)

                        GER_LANG -> languageSettingsPresenter.changeLang(UserPreferences.Language.GER)

                    }

                    languageSettingsPresenter.updateLang()

                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spinnerLang.adapter =
            ArrayAdapter(applicationContext,
                R.layout.support_simple_spinner_dropdown_item, listOf(RUS_LANG, ENG_LANG, GER_LANG))

        spinnerInterval.adapter = ArrayAdapter(applicationContext,
            R.layout.tv_spinner, listOf("2 hours", "4 hours", "8 hours", "12 hours", "24 hours"))

        spinnerInterval.setSelection(when (UserPreferences.notifInterval) {

            2 -> 0
            4 -> 1
            8 -> 2
            12 -> 3
            24 -> 4

            else -> throw Exception("The interval was set wrong")
        })

        inputNotificationCity.setText(UserPreferences.notifCity)

        btnApplyNotifications.setOnClickListener {

            btnApplyPresenter.check(notificationCity = inputNotificationCity.text.toString(),
                                    hours = when (spinnerInterval.selectedItemPosition) {

                                        0 -> 2
                                        1 -> 4
                                        2 -> 8
                                        3 -> 12
                                        4 -> 24
                                        else -> -1
                                    })
        }

        cbFullscreen.setOnCheckedChangeListener { compoundButton, b ->

            UserPreferences.fullscreen = b

        }

    }

    override fun lowSnow() {
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.low_snow, null))
    }

    override fun snow() {
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.low_snow, null))
    }

    override fun clear() =
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.clear, null))

    override fun cloudy() =
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.cloud, null))

    override fun lowCloudy() =
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.low_cloud, null))

    override fun rainy() =
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.rain, null))

    override fun humid() =
        settingsBackground.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.humid, null))

    override fun updateLang(lang: UserPreferences.Language) {

        when (lang) {

            UserPreferences.Language.RUS -> imgLang.
                setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.russia, null))

            UserPreferences.Language.ENG -> imgLang.
                setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.england, null))

            UserPreferences.Language.GER -> imgLang.
                setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.germany, null))

        }

    }

    override fun success() {
        inputNotificationCity.reformat()
        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()
        inputNotificationCity.hideKeyboard(applicationContext)
    }

    override fun fail(msg: String) {
        inputNotificationCity.reformat()
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}