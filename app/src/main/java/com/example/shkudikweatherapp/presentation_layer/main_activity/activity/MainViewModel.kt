package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import android.annotation.SuppressLint
import android.app.Application
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.data_layer.providers.Helper.ABS_ZERO
import com.example.shkudikweatherapp.data_layer.providers.Helper.METER_PER_SEC
import com.example.shkudikweatherapp.data_layer.providers.Helper.METER_PER_SEC_RUS
import com.example.shkudikweatherapp.data_layer.providers.Helper.PERCENT
import com.example.shkudikweatherapp.data_layer.providers.Helper.fahrenheit
import com.example.shkudikweatherapp.data_layer.http_client.ApplicationRetrofitClient
import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity.Companion.isMoreInfoOpened
import com.example.shkudikweatherapp.data_layer.providers.Helper.getMainDescription
import com.example.shkudikweatherapp.data_layer.providers.Helper.isNightTime
import com.example.shkudikweatherapp.data_layer.providers.Helper.reformat
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
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import com.example.shkudikweatherapp.data_layer.providers.Helper.locationDeniedError
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.isLocationApplied
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.math.roundToInt

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val retrofitClient = ApplicationRetrofitClient(this)

    // Weather
    var isNight = MutableLiveData<Boolean>()
    var temp = MutableLiveData<String>()
    var state = MutableLiveData<MainStates>()
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
        CoroutineScope(Main).launch {
            while (true) {
                if (!isMoreInfoOpened)
                    load(this)
                delay(10000)
            }
    }

    suspend fun load(coroutineScope: CoroutineScope) {

            val weather =
                coroutineScope.async { withContext(IO) {
                    when (searchMode) {

                        UserPreferences.SearchMode.CITY -> retrofitClient.loadWeather(selectedCity)

                        UserPreferences.SearchMode.GEO -> retrofitClient.loadWeather(selectedLat, selectedLon)

                    }
                } }.await()

            val timeUTC =
                coroutineScope.async { withContext(IO) {

                    retrofitClient.loadTimeUTC()

                } }.await()

            val forecast =
                coroutineScope.async { withContext(IO) {
                    when (searchMode) {

                        UserPreferences.SearchMode.CITY -> retrofitClient.loadForecast(selectedCity)

                        UserPreferences.SearchMode.GEO -> retrofitClient.loadForecast(selectedLat, selectedLon)

                    }
                } }.await()

            if (weather != null && timeUTC != null && forecast != null) {

                succeed(weather, timeUTC, forecast)

            }
        }

    private suspend fun succeed(weather: Weather, timeUTC: TimeUTC, forecast: Forecast) {

         if (searchMode == UserPreferences.SearchMode.CITY)
             WeatherProvider.addHelpCity(selectedCity)

         // Received weather
         val receivedTemp = weather.main.temp.toInt() - ABS_ZERO
         val receivedHumidity = weather.main.humidity.toString() + PERCENT
         val receivedWind = weather.wind.speed.toInt()
         val receivedMainDesc = weather.weather!![0].main
         val receivedDesc = weather.weather[0].description

         // Received time
         val receivedTime = timeUTC.currentDateTime.substring(11..15)

         val hoursDelta = weather.timezone / 3600
         val delta = receivedTime.substring(0..1).toInt() + hoursDelta

         // Received forecast
         val receivedFTemp1 = weather.main.temp.toInt()
         val receivedFTemp2 = forecast.list[1].main.temp.toInt()
         val receivedFTemp3 = forecast.list[2].main.temp.toInt()

         val receivedFFeels1 = weather.main.feels_like.toInt()
         val receivedFFeels2 = forecast.list[1].main.feels_like.toInt()
         val receivedFFeels3 = forecast.list[2].main.feels_like.toInt()

         val receivedFWind1 = weather.wind.speed.toInt()
         val receivedFWind2 = forecast.list[1].windModel.speed.toInt()
         val receivedFWind3 = forecast.list[2].windModel.speed.toInt()

         val receivedFWindDir1 = weather.wind.deg
         val receivedFWindDir2 = forecast.list[1].windModel.deg
         val receivedFWindDir3 = forecast.list[2].windModel.deg

         val receivedFPressure1 = weather.main.pressure
         val receivedFPressure2 = forecast.list[1].main.pressure
         val receivedFPressure3 = forecast.list[2].main.pressure

         // applying received info

         //
         withContext(Default) {
             time.postValue(
                 (when {
                     (delta >= 24) -> (delta - 24)
                     (delta < 0) -> (delta + 24)
                     else -> delta
                 }).toString() + receivedTime.substring(2..4)
             )
         }
         //
         withContext(Default) {
             desc.postValue("$receivedDesc, ${time.value!!}")
             isNight.postValue(isNightTime(time.value!!))
         }
         //
         withContext(Default) {
             mainDesc.postValue(getMainDescription(isNight.value!!, receivedMainDesc))
         }


        // Setting forecast

        fTemp.value(arrayOf(setTemp(receivedFTemp1), setTemp(receivedFTemp2), setTemp(receivedFTemp3)))

        fFeels.value(arrayOf(setTemp(receivedFFeels1), setTemp(receivedFFeels2), setTemp(receivedFFeels3)))

        fWind.value(arrayOf(setWindSpeed(receivedFWind1), setWindSpeed(receivedFWind2), setWindSpeed(receivedFWind3)))

        fWindDir.value(arrayOf(setWindDirection(receivedFWindDir1),
                               setWindDirection(receivedFWindDir2),
                               setWindDirection(receivedFWindDir3)))

        fHumidity.value(arrayOf(weather.main.humidity.toString() + PERCENT,
                                forecast.list[1].main.humidity.toString() + PERCENT,
                                forecast.list[2].main.humidity.toString() + PERCENT))

        fPressure.value(arrayOf(setPressure(receivedFPressure1),
                                setPressure(receivedFPressure2),
                                setPressure(receivedFPressure3)))

        // Setting current weather

        temp.value(
            (if (degreeUnit == UserPreferences.TemperatureUnit.DEG_C)
                        (receivedTemp) else (receivedTemp).fahrenheit()).toString() + degreeUnit.str
        )

        humidity.value(receivedHumidity)

        wind.value(
            receivedWind.toString() +
                    if (language != UserPreferences.Language.RUS) METER_PER_SEC else METER_PER_SEC_RUS
        )

        state.value(MainStates.UPDATED)

    }

    internal fun applyCity(activity: MainActivity) { with(activity) {

            if (input_city.text!!.isNotEmpty()) {

                searchMode = UserPreferences.SearchMode.CITY

                input_city.reformat()
                selectedCity = input_city.text.toString()
                stateImpl.setState(MainStates.CITY_APPLIED)
                stateImpl.setState(MainStates.LOADING)

                CoroutineScope(Main).launch {
                    state.value(MainStates.LOADING)
                    viewModel.load(this)
                }

            } else {

                Toast.makeText(applicationContext, Helper.emptyInputErrorMessage, Toast.LENGTH_LONG)
                    .show()

            }
        }
    }

    @SuppressLint("MissingPermission")
    internal fun applyLocation(activity: MainActivity) {

        if (isLocationApplied) {

            state.value(MainStates.LOADING)

            try {

                val locationManager = activity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f) { location ->

                    activity.stateImpl.setState(MainStates.CITY_APPLIED)
                    activity.stateImpl.setState(MainStates.LOADING)

                    selectedLon = ((location.longitude * 1000).roundToInt() / 1000f)
                    selectedLat = ((location.latitude * 1000).roundToInt() / 1000f)

                    searchMode = UserPreferences.SearchMode.GEO
                    activity.boardImpl.setUserLocationTitle()
                    CoroutineScope(Main).launch { load(this) }

                }

            } catch (e: Throwable) {}

        } else {

            activity.locationAvailabilityImpl.showError(locationDeniedError)

        }
    }
}