package com.example.shkudikweatherapp.presentation_layer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.data_layer.providers.Helper.ABS_ZERO
import com.example.shkudikweatherapp.data_layer.providers.Helper.METER_PER_SEC
import com.example.shkudikweatherapp.data_layer.providers.Helper.METER_PER_SEC_RUS
import com.example.shkudikweatherapp.data_layer.providers.Helper.PERCENT
import com.example.shkudikweatherapp.data_layer.providers.Helper.fahrenheit
import com.example.shkudikweatherapp.data_layer.http_client.Retrofit2Client
import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity.Companion.isMoreInfoOpened
import com.example.shkudikweatherapp.data_layer.providers.Helper.getMainDescription
import com.example.shkudikweatherapp.data_layer.providers.Helper.isNightTime
import com.example.shkudikweatherapp.data_layer.providers.Helper.setPressure
import com.example.shkudikweatherapp.data_layer.providers.Helper.setTemp
import com.example.shkudikweatherapp.data_layer.providers.Helper.setWindDirection
import com.example.shkudikweatherapp.data_layer.providers.Helper.setWindSpeed
import com.example.shkudikweatherapp.data_layer.providers.Helper.value
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.degreeUnit
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedCity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedLat
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedLon
import com.example.shkudikweatherapp.presentation_layer.states.MainDescription
import com.example.shkudikweatherapp.presentation_layer.states.States
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val retrofitClient = Retrofit2Client(this)

    // Weather
    var isNight = MutableLiveData<Boolean>()
    var temp = MutableLiveData<String>()
    var state = MutableLiveData<States>()
    var mainDesc = MutableLiveData<MainDescription>()
    var desc = MutableLiveData<String>()
    var humidity = MutableLiveData<String>()
    var wind = MutableLiveData<String>()

    // Time
    var time = MutableLiveData<String>()

    // Forecast (1, 2, 3 parts)
    var fTemp = MutableLiveData<Array<String>>()
    var fFeels = MutableLiveData<Array<String>>()
    var fHumidity = MutableLiveData<Array<String>>()
    var fWind = MutableLiveData<Array<String>>()
    var fWindDir = MutableLiveData<Array<String>>()
    var fPressure = MutableLiveData<Array<String>>()

    fun update() =
        CoroutineScope(IO).launch {

            while (true) {

                if (!isMoreInfoOpened)
                    load()
                delay(10000)

            }
    }

    fun load() {

        GlobalScope.launch(IO) {

            val weather =
                async {
                    if (searchMode == UserPreferences.SearchMode.CITY) {

                        retrofitClient.loadWeather(selectedCity)

                    } else {

                        retrofitClient.loadLocalWeather(selectedLat, selectedLon)

                    }

                }.await()

            val timeUTC =
                async { retrofitClient.loadTimeUTC() }.await()

            val forecast =
                async {
                    if (searchMode == UserPreferences.SearchMode.CITY) {

                        retrofitClient.loadForecast(selectedCity)

                    } else {

                        retrofitClient.loadForecast(selectedLat, selectedLon)

                    }

                }.await()


            if (weather != null && timeUTC != null && forecast != null) {

                succeed(weather, timeUTC, forecast)

            }


        }

    }

    private suspend fun succeed(weather: Weather, timeUTC: TimeUTC, forecast: Forecast) {

        withContext(Main) {

            // Received weather
            val receivedTemp = weather.main.temp.toInt() - ABS_ZERO
            val receivedHumidity = weather.main.humidity.toString() + PERCENT
            val receivedWind = weather.wind.speed.toInt()
            val receivedMainDesc = weather.weather!![0].main
            val receivedDesc = weather.weather[0].description

            // Received time
            val receivedTime = timeUTC.currentDateTime.substring(11..15)
            //
            val hoursDelta = weather.timezone / 3600
            val delta = receivedTime.substring(0..1).toInt() + hoursDelta
            //

            // Received forecast
            val receivedFTemp1 = forecast.list[1].main.temp.toInt()
            val receivedFTemp2 = forecast.list[2].main.temp.toInt()
            val receivedFTemp3 = forecast.list[3].main.temp.toInt()

            val receivedFFeels1 = forecast.list[1].main.feels_like.toInt()
            val receivedFFeels2 = forecast.list[2].main.feels_like.toInt()
            val receivedFFeels3 = forecast.list[3].main.feels_like.toInt()

            val receivedFWind1 = forecast.list[1].windModel.speed.toInt()
            val receivedFWind2 = forecast.list[2].windModel.speed.toInt()
            val receivedFWind3 = forecast.list[3].windModel.speed.toInt()

            val receivedFWindDir1 = forecast.list[1].windModel.deg
            val receivedFWindDir2 = forecast.list[2].windModel.deg
            val receivedFWindDir3 = forecast.list[3].windModel.deg

            val receivedFPressure1 = forecast.list[1].main.pressure
            val receivedFPressure2 = forecast.list[2].main.pressure
            val receivedFPressure3 = forecast.list[3].main.pressure


            // applying received info

            if (searchMode == UserPreferences.SearchMode.CITY)
                WeatherProvider.addHelpCity(selectedCity)


            withContext(IO) {

                time.postValue(
                    (when {
                        (delta >= 24) -> (delta - 24)
                        (delta < 0) -> (delta + 24)
                        else -> delta
                    }).toString() + receivedTime.substring(2..4)
                )

            }


            withContext(IO) {

                desc.postValue("$receivedDesc, ${time.value!!}")
                isNight.postValue(isNightTime(time.value!!))

            }

            withContext(IO) {

                getMainDescription(isNight.value!!, receivedMainDesc).apply {

                    mainDesc.postValue(this)

                }

            }

            // Setting forecast
            withContext(Main) {

                fTemp.value(arrayOf(setTemp(receivedFTemp1), setTemp(receivedFTemp2), setTemp(receivedFTemp3)))

                fFeels.value(arrayOf(setTemp(receivedFFeels1), setTemp(receivedFFeels2), setTemp(receivedFFeels3)))

                fWind.value(arrayOf(setWindSpeed(receivedFWind1), setWindSpeed(receivedFWind2), setWindSpeed(receivedFWind3)))

                fWindDir.value(arrayOf(setWindDirection(receivedFWindDir1),
                                       setWindDirection(receivedFWindDir2),
                                       setWindDirection(receivedFWindDir3)))

                fHumidity.value(arrayOf(forecast.list[1].main.humidity.toString() + PERCENT,
                                        forecast.list[2].main.humidity.toString() + PERCENT,
                                        forecast.list[3].main.humidity.toString() + PERCENT))

                fPressure.value(arrayOf(setPressure(receivedFPressure1),
                                        setPressure(receivedFPressure2),
                                        setPressure(receivedFPressure3)))


            }

            temp.value(
                (if (degreeUnit == UserPreferences.DegreeUnit.DEG_C)
                            (receivedTemp) else (receivedTemp).fahrenheit()).toString() + degreeUnit.str
            )

            humidity.value(receivedHumidity)

            wind.value(
                receivedWind.toString() +
                        if (language != UserPreferences.Language.RUS) METER_PER_SEC else METER_PER_SEC_RUS
            )

            state.value(States.UPDATED)

        }

    }

}