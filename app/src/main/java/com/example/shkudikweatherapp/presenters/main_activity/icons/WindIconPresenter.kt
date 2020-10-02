package com.example.shkudikweatherapp.presenters.main_activity.icons

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.views.main_activity.WindIcon


@InjectViewState
class WindIconPresenter : MvpPresenter<WindIcon>() {

    fun showWind() = viewState.showWind(WeatherProvider.wind)

}