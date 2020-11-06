package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.Language.*
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface Language {

    fun setImgLanguage(language: UserPreferences.Language)
    fun setLanguage(language: UserPreferences.Language)

}

class LanguageImpl(private val activity: SettingsActivity) : Language {

    override fun setImgLanguage(language: UserPreferences.Language) { with(activity) {

        imgLang.setImageDrawable(when (language) {

                GER -> ContextCompat.getDrawable(this, R.drawable.germany)
                ENG -> ContextCompat.getDrawable(this, R.drawable.england)
                RUS -> ContextCompat.getDrawable(this, R.drawable.russia)

            })
        }
    }

    override fun setLanguage(language: UserPreferences.Language) { with(activity) {

            when (language) {

                ENG -> {

                    imgLang.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.england))
                    UserPreferences.language = ENG

                }

                RUS -> {

                    imgLang.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.russia))
                    UserPreferences.language = RUS

                }

                GER -> {

                    imgLang.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.germany))
                    UserPreferences.language = GER

                }
            }
        }
    }
}