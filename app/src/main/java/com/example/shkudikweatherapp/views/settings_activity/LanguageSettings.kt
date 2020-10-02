package com.example.shkudikweatherapp.views.settings_activity

import com.arellomobile.mvp.MvpView
import com.example.shkudikweatherapp.providers.UserPreferences

interface LanguageSettings : MvpView {

    fun updateLang(lang: UserPreferences.Language)

}