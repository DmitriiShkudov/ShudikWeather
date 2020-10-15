package com.example.shkudikweatherapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.activities.SettingsActivity
import com.example.shkudikweatherapp.providers.Helper.ABS_ZERO
import com.example.shkudikweatherapp.providers.Helper.METER_PER_SEC
import com.example.shkudikweatherapp.providers.Helper.METER_PER_SEC_RUS
import com.example.shkudikweatherapp.providers.Helper.PERCENT
import com.example.shkudikweatherapp.providers.Helper.fahrenheit
import com.example.shkudikweatherapp.http_client.Retrofit2Client
import com.example.shkudikweatherapp.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.Helper
import com.example.shkudikweatherapp.providers.Helper.getMainDescription
import com.example.shkudikweatherapp.providers.Helper.isNightTime
import com.example.shkudikweatherapp.providers.Helper.value
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.MainDescription
import com.example.shkudikweatherapp.states.State
import com.example.shkudikweatherapp.states.WeatherState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.internal.wait

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClient = Retrofit2Client(this)

    var isNight = MutableLiveData<Boolean>()
    var temp = MutableLiveData<String>()
    var state = MutableLiveData<State>()
    var mainDesc = MutableLiveData<MainDescription>()
    var desc = MutableLiveData<String>()
    var humidity = MutableLiveData<String>()
    var wind = MutableLiveData<String>()

    fun stateChangingCity() = this.state.value(State.CHANGING_CITY)
    fun stateChangingCityCancelled() = this.state.value(State.CHANGING_CITY_CANCELLED)
    fun stateCityApplied() = this.state.value(State.CITY_APPLIED)
    fun stateLoading() = this.state.postValue(State.LOADING)
    fun stateConnectionError() = this.state.postValue(State.BAD_CONNECTION)
    fun stateWrongCity() = this.state.postValue(State.WRONG_CITY)

    fun update() =
        CoroutineScope(IO).launch {

            while (true) {

                load()
                delay(8000)

            }
    }

    fun load() {

        GlobalScope.launch(IO) {

            val weather =
                 async { retrofitClient.loadWeather(WeatherProvider.selectedCity) }.await()

            val timeUTC =
                 async { retrofitClient.loadTimeUTC() }.await()

            if (weather != null && timeUTC != null) {

                succeed(weather, timeUTC)

            }
        }
    }

    private suspend fun succeed(weather: Weather, timeUTC: TimeUTC) {

        val receivedTemp = weather.main.temp.toInt() - ABS_ZERO
        val receivedHumidity = weather.main.humidity.toString() + PERCENT
        val receivedWind = weather.wind.speed.toInt()
        val receivedMainDesc = weather.weather!![0].main
        val desc = weather.weather[0].description
        val cutTimeUTC = timeUTC.currentDateTime.substring(11..15)
        val hoursDelta = weather.timezone / 3600
        val delta = cutTimeUTC.substring(0..1).toInt() + hoursDelta
        val time = (when {
            (delta >= 24) -> (delta - 24)
            (delta < 0) -> (delta + 24)
            else -> delta
            }).toString() + cutTimeUTC.substring(2..4)


        Log.d("time", cutTimeUTC)
        Log.d("hours delta", hoursDelta.toString())

        // applying received info

        WeatherProvider.addHelpCity(WeatherProvider.selectedCity)

        withContext(IO) {

            isNight.postValue(isNightTime(time))
            this@MainViewModel.desc.postValue("$desc, $time")

            withContext(Main) {

                getMainDescription(isNight.value!!, receivedMainDesc).apply {

                    mainDesc.postValue(this)
                    SettingsActivity.mainDesc = this

                }

            }

        }

        this.temp.postValue((

             if (UserPreferences.degreeUnit == UserPreferences.DegreeUnit.DEG_C)
                 (receivedTemp) else (receivedTemp).fahrenheit()).toString() + UserPreferences.degreeUnit.str)

        this.humidity.postValue(receivedHumidity)

        this.wind.postValue(receivedWind.toString() +
             if (UserPreferences.language != UserPreferences.Language.RUS) METER_PER_SEC else METER_PER_SEC_RUS)

        this.state.postValue(State.UPDATED)

    }

}