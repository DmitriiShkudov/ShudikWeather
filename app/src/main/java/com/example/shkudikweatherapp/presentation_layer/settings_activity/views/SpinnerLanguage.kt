package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.Language.*
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface SpinnerLanguage {

    fun init()
    fun setLanguage(language: UserPreferences.Language)

}

class SpinnerLanguageImpl(private val activity: SettingsActivity): SpinnerLanguage {

    override fun init() { with(activity) {

                spinnerLang.adapter = ArrayAdapter(
                    applicationContext,
                    R.layout.support_simple_spinner_dropdown_item,
                    listOf(ENG.str, RUS.str, GER.str))

                var i = 0 // spinner bug
                spinnerLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                        if (++i > 1) {

                            when (position) {

                                0 -> languageImpl.setLanguage(ENG)
                                1 -> languageImpl.setLanguage(RUS)
                                2 -> languageImpl.setLanguage(GER)
                            }

                            localeImpl.setLocale()

                        }
                    }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }
        }
    }

    override fun setLanguage(language: UserPreferences.Language) { with(activity) {

            spinnerLang.setSelection(when (language) {

                    ENG -> 0
                    RUS -> 1
                    GER -> 2

                })
        }
    }
}