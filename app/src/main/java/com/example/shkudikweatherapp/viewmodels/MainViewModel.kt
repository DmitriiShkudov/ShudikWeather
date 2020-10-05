package com.example.shkudikweatherapp.viewmodels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shkudikweatherapp.activities.MainActivity
import com.example.shkudikweatherapp.helpers.Helper
import com.example.shkudikweatherapp.helpers.Helper.CELSIUS_DEGREE
import com.example.shkudikweatherapp.helpers.Helper.METER_PER_SEC
import com.example.shkudikweatherapp.helpers.Helper.PERCENT
import com.example.shkudikweatherapp.http_client.Retrofit2Client
import com.example.shkudikweatherapp.http_client.RetrofitClient
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var state = MutableLiveData<State>()
    var city = MutableLiveData<String>()
    var temp = MutableLiveData<String>()
    var desc = MutableLiveData<String>()
    var humidity = MutableLiveData<String>()
    var wind = MutableLiveData<String>()

    var retrofitClient = Retrofit2Client(this)

    suspend fun changeCityAndLoad(city: String) {

        this.city.value = city

        this.state.value = State.LOADING

        CoroutineScope(IO).launch {

            this@MainViewModel.retrofitClient.loadWeather(this@MainViewModel.city.value!!)

        }

    }

    fun weatherLoaded(weather: Weather) {

        WeatherProvider.selectedCity = this.city.value!!
        this.state.value = State.UPDATED

        this.temp.value = weather.main.temp.toString() + CELSIUS_DEGREE
        this.desc.value = weather.weather!![0].description
        this.humidity.value = weather.main.humidity.toString() + PERCENT
        this.wind.value = weather.wind.speed.toString() + METER_PER_SEC

    }

    fun connectionError() {

        this.state.value = State.BAD_CONNECTION

    }



}