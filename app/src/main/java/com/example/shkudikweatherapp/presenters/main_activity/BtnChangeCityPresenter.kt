package com.example.shkudikweatherapp.presenters.main_activity

import android.os.Handler
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.http_client.RetrofitClient
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.views.main_activity.BtnChangeCity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

@InjectViewState
class BtnChangeCityPresenter : MvpPresenter<BtnChangeCity>() {

    fun enteringCity() = viewState.enteringCity()

    fun applyCity(city: String) {

        WeatherProvider.addHelpCity(city)
        WeatherProvider.selectedCity = city
        viewState.applyCity()

    }
}