package com.example.shkudikweatherapp.presenters.main_activity.icons

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.http_client.RetrofitClient
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.views.main_activity.WeatherInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@InjectViewState
class WeatherInfoPresenter: MvpPresenter<WeatherInfo>() {

    suspend fun loadData() {

        CoroutineScope(Dispatchers.IO).launch {

            RetrofitClient.loadWeather(WeatherProvider.selectedCity)

            CoroutineScope(Main).launch {

                viewState.showWeather(
                    WeatherProvider.description,
                    WeatherProvider.humidity,
                    WeatherProvider.temperature,
                    WeatherProvider.wind
                )

            }

        }

    }
}