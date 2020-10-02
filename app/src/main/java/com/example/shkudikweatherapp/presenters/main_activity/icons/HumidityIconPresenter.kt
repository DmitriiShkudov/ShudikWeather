package com.example.shkudikweatherapp.presenters.main_activity.icons

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.views.main_activity.HumidityIcon


@InjectViewState
class HumidityIconPresenter : MvpPresenter<HumidityIcon>() {

    fun showHumidity() = viewState.showHumidity(WeatherProvider.humidity)

}