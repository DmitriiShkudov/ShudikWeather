package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Locale
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*
import java.security.Key

interface Language {

    fun setImgLanguage(language: UserPreferences.Language)
    fun setLanguage(language: UserPreferences.Language)

}

class LanguageImpl(private val activity: SettingsActivity) : Language {

    override fun setImgLanguage(language: UserPreferences.Language) { with(activity) {

        imgLang.setImageDrawable(
            when (language) {

                UserPreferences.Language.GER -> ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.germany,
                    null
                )
                UserPreferences.Language.ENG -> ResourcesCompat.getDrawable(resources,
                        R.drawable.england,
                    null
                )
                else -> ResourcesCompat.getDrawable(resources, R.drawable.russia, null)
            })
        }
    }

    override fun setLanguage(language: UserPreferences.Language) { with(activity) {

            when (language) {

                    UserPreferences.Language.ENG -> {

                        imgLang.setImageDrawable(
                            ResourcesCompat.getDrawable(
                             resources,
                              R.drawable.england,
                              null))

                        UserPreferences.language = UserPreferences.Language.ENG
                    }

                UserPreferences.Language.RUS -> {

                    imgLang.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.russia,
                            null
                        )
                    )
                    UserPreferences.language = UserPreferences.Language.RUS

                }

                UserPreferences.Language.GER -> {

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
        }
    }
}