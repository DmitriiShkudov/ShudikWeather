package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import android.annotation.SuppressLint
import android.app.Application
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper.ABS_ZERO
import com.example.shkudikweatherapp.data_layer.providers.Helper.fahrenheit
import com.example.shkudikweatherapp.data_layer.http_client.ApplicationRetrofitClient
import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
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
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.LOADING_DELAY
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.emptyInputMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.locationIsDeniedMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.locationIsUnavailable
import com.example.shkudikweatherapp.data_layer.providers.Helper.Objects.location
import com.example.shkudikweatherapp.data_layer.providers.Helper.Units.windUnit
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.isLocationApplied
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.math.roundToInt

class MainViewModel(val app: Application) : AndroidViewModel(app) {

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
                if (state.value != MainStates.MORE_INFO && state.value != MainStates.CHANGING_CITY)
                    load(this)
                delay(LOADING_DELAY)
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

         if (searchMode == UserPreferences.SearchMode.CITY) {

             WeatherProvider.addHelpCity(selectedCity)

         }

         WeatherPresenter.loadedPresentation(weather, timeUTC, forecast, this)

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

                boardImpl.showError(emptyInputMessage)

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    internal fun applyLocation(activity: MainActivity) { with(activity) {

            if (isLocationApplied) {

                try {

                    stateImpl.setState(MainStates.CITY_APPLIED)
                    stateImpl.setState(MainStates.LOADING)

                    val locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            60000,
                            0f,
                            mainExecutor
                        ) { location ->

                            selectedLon = ((location.longitude * 1000).roundToInt() / 1000f)
                            selectedLat = ((location.latitude * 1000).roundToInt() / 1000f)

                            searchMode = UserPreferences.SearchMode.GEO
                            CoroutineScope(Main).launch { load(this) }

                        }
                    } else {

                        locationAvailabilityImpl.showError(locationIsUnavailable)

                    }

                } catch (e: Throwable) {

                    locationAvailabilityImpl.showError(locationIsUnavailable)

                }

            } else {

                locationAvailabilityImpl.showError(locationIsDeniedMessage)

            }
        }
    }
}