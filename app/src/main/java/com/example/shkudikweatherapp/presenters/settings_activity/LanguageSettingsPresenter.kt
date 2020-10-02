package com.example.shkudikweatherapp.presenters.settings_activity

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.views.settings_activity.LanguageSettings

@InjectViewState
class LanguageSettingsPresenter : MvpPresenter<LanguageSettings>() {

    companion object {

        const val RUS_LANG = "Русский"
        const val ENG_LANG = "English"
        const val GER_LANG = "Deutsch"

    }

    fun updateLang() = viewState.updateLang(UserPreferences.language)

    fun changeLang(lang: UserPreferences.Language) {

        UserPreferences.language = lang

    }

}