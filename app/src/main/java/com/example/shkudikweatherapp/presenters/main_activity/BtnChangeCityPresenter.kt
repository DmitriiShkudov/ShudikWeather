package com.example.shkudikweatherapp.presenters.main_activity

import android.os.Handler
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.http_client.HttpClient
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.threads.ThreadManager
import com.example.shkudikweatherapp.views.main_activity.BtnChangeCity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@InjectViewState
class BtnChangeCityPresenter : MvpPresenter<BtnChangeCity>() {

    companion object {

        const val UPDATE_CITY = 1

    }

    val handler = Handler {

        when (it.what) {

            UPDATE_CITY -> {

                viewState.showResult(success = true)
                ThreadManager.restartWeatherThread()

                /*Thread {

                    UserPreferences.db.infoDao().addInfo(Info( 0,"22.32.32","Want a girl", "rfrfrf"))
                    sleep(3000)
                    UserPreferences.db.infoDao().readAllData().value!!.forEach {

                        Log.d("Holy shit's", it.toString())

                    }

                }.start() */

            }

            else -> {

                viewState.showResult(success = false)

            }

        }

        true

    }

    fun enteringCity() = viewState.enteringCity()


    fun applyCity() = viewState.applyCity()


    fun executeAndShowResult(city: String) {

        WeatherProvider.addHelpCity(city)
        WeatherProvider.selectedCity = city
        Log.d("SELECTED CITY ---->", WeatherProvider.selectedCity)

        CoroutineScope(IO).launch {

            HttpClient.loadWeatherInfo(city)

            CoroutineScope(Main).launch {

                viewState.showResult(true)

            }
        }

    }

}