package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

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

                GER -> ResourcesCompat.getDrawable(resources, R.drawable.germany, null)

                ENG -> ResourcesCompat.getDrawable(resources, R.drawable.england, null)

                RUS -> ResourcesCompat.getDrawable(resources, R.drawable.russia, null)
            })
        }
    }

    override fun setLanguage(language: UserPreferences.Language) { with(activity) {

            when (language) {

                ENG -> {

                    imgLang.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.england, null))
                    UserPreferences.language = ENG

                }

                RUS -> {

                    imgLang.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.russia, null))
                    UserPreferences.language = RUS

                }

                GER -> {

                    imgLang.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.germany, null))
                    UserPreferences.language = GER

                }
            }
        }
    }
}