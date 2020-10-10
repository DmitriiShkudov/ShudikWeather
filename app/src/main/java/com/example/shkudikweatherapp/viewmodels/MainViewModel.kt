package com.example.shkudikweatherapp.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shkudikweatherapp.activities.MainActivity
import com.example.shkudikweatherapp.helpers.Helper
import com.example.shkudikweatherapp.helpers.Helper.ABS_ZERO
import com.example.shkudikweatherapp.helpers.Helper.METER_PER_SEC
import com.example.shkudikweatherapp.helpers.Helper.PERCENT
import com.example.shkudikweatherapp.helpers.Helper.fahrenheit
import com.example.shkudikweatherapp.http_client.Retrofit2Client
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.State
import com.example.shkudikweatherapp.states.WeatherState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Thread.sleep

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var state = MutableLiveData<State>()
    var weatherState = MutableLiveData<WeatherState>()
    var city = MutableLiveData<String>()
    var temp = MutableLiveData<String>()
    var desc = MutableLiveData<String>()
    var humidity = MutableLiveData<String>()
    var wind = MutableLiveData<String>()

    private val retrofitClient = Retrofit2Client(this)

    fun changingCity() {

        this.state.value = State.CHANGING_CITY

    }

    fun update() =

        CoroutineScope(IO).launch {

            while (true) {

                this@MainViewModel.retrofitClient.loadWeather(WeatherProvider.selectedCity)
                delay(8000)

            }

    }

    fun load() = this@MainViewModel.retrofitClient.loadWeather(WeatherProvider.selectedCity)

    fun applyCity(city: String) {

        WeatherProvider.selectedCity = city
        this.state.postValue(State.LOADING)

    }

    fun weatherLoaded(weather: Weather) {

        this.state.postValue(State.UPDATED)

        //
        this.temp.postValue(( if (UserPreferences.degreeUnit == UserPreferences.DegreeUnit.DEG_C)

                (weather.main.temp.toInt() - ABS_ZERO) else (weather.main.temp.toInt() - ABS_ZERO).
                    fahrenheit()).toString() +  UserPreferences.degreeUnit.str)

        this.humidity.postValue(weather.main.humidity.toString() + PERCENT)
        this.wind.postValue(weather.wind.speed.toInt().toString() + METER_PER_SEC)
        this.desc.value = weather.weather!![0].description
        //
        WeatherProvider.description = this.desc.value!!
        this@MainViewModel.weatherState.value = WeatherProvider.weatherState

    }

    fun wrongCity() {

        this.state.postValue(State.WRONG_CITY)
        this.desc.value = "This city is not found"
        WeatherProvider.description = "This city is not found"

    }

    fun connectionError() {

        this.state.postValue(State.BAD_CONNECTION)

    }

}