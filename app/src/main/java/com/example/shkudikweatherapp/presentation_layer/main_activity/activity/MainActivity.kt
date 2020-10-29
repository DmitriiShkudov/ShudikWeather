package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY_INPUT_ERROR_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY_INPUT_ERROR_GER
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY_INPUT_ERROR_RUS
import com.example.shkudikweatherapp.data_layer.providers.Helper.emptyInputErrorMessage
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.isLocationApplied
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isNight
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedCity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedLat
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedLon
import com.example.shkudikweatherapp.presentation_layer.fragments.MoreInfo
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.*
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import com.example.shkudikweatherapp.data_layer.states.States
import com.example.shkudikweatherapp.presentation_layer.viewmodels.MainViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


internal fun View.showKeyboard(context: Context) =

    (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
        showSoftInput(this, 0)

internal fun View.hideKeyboard(context: Context) =

    (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
        hideSoftInputFromWindow(this.windowToken, 0)

internal fun EditText.reformat() {

    val text = this.text.toString()
    this.setText("${text[0].toUpperCase()}${text.toLowerCase().substring(1..text.length - 1)}")

}


class MainActivity : AppCompatActivity() {

    companion object {

        var isMoreInfoOpened = false
        const val FRAGMENT_TAG = "fragment"

    }

    lateinit var viewModel: MainViewModel

    //
    lateinit var backgroundImpl: BackgroundImpl
        private set

    lateinit var boardImpl: BoardImpl
        private set

    lateinit var changeableSearchModeImpl: ChangeableSearchModeImpl
        private set

    lateinit var moreInfoImpl: MoreInfoImpl
        private set

    lateinit var localeImpl: LocaleImpl
        private set

    lateinit var locationAvailabilityImpl: LocationAvailabilityImpl
        private set

    lateinit var recyclerHelpImpl: RecyclerHelpImpl
        private set

    lateinit var stateImpl: StateImpl
        private set

    lateinit var timeModeImpl: TimeModeImpl
        private set

    lateinit var weatherIconsImpl: WeatherIconsImpl
        private set
    //

    override fun onResume() {

        super.onResume()

        CoroutineScope(Main).launch { viewModel.load(this) }

        localeImpl.setLocale()

        if (searchMode == UserPreferences.SearchMode.GEO) {
            boardImpl.setUserLocationTitle()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // init
        WeatherProvider.context = applicationContext
        UserPreferences.context = applicationContext
        viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        if (UserPreferences.fullscreen) setTheme(R.style.fullscreen)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backgroundImpl = BackgroundImpl(this)
        boardImpl = BoardImpl(this)
        changeableSearchModeImpl = ChangeableSearchModeImpl(this)
        moreInfoImpl = MoreInfoImpl(this)
        localeImpl =  LocaleImpl(this)
        locationAvailabilityImpl = LocationAvailabilityImpl(this)
        recyclerHelpImpl = RecyclerHelpImpl(this, boardImpl)
        timeModeImpl = TimeModeImpl(this)
        weatherIconsImpl = WeatherIconsImpl(this)
        stateImpl = StateImpl(this)

        if (!isLocationApplied)
            locationAvailabilityImpl.askPermission()

        if (searchMode == UserPreferences.SearchMode.CITY)
            boardImpl.setCity() else boardImpl.setUserLocationTitle()

        recyclerHelpImpl.update()
        stateImpl.setState(States.LOADING)
        boardImpl.setOnEnterClickEvent()
        viewModel.update()

        // On Clicks //

        btn_change_city.setOnClickListener {

            stateImpl.setState(States.CHANGING_CITY)

            if (isMoreInfoOpened)
                moreInfoImpl.detach()

        }

        btn_geo.setOnClickListener {

            stateImpl.setState(States.CHANGING_CITY_CANCELLED)

            if (isMoreInfoOpened)
                moreInfoImpl.detach()

            applyLocationIfAllowed()

        }

        btn_apply_city.setOnClickListener {

            viewModel.applyCity(this)

        }

        btn_settings.setOnClickListener {

            if (isMoreInfoOpened)
                moreInfoImpl.detach()

            openSettings()

        }

        tvDescriptionIcon.setOnClickListener {

            if (isSelectedCityExists) {

                stateImpl.setState(States.MORE_INFO)
                isMoreInfoOpened = true

            }

        }

        // observers //

        viewModel.state.observe(this, { stateImpl.setState(it) })

        viewModel.mainDesc.observe(this) {
            mainDesc = it
            backgroundImpl.setBackground(it)
        }

        viewModel.temp.observe(this) { weatherIconsImpl.setTemperature(it) }

        viewModel.humidity.observe(this) { weatherIconsImpl.setHumidity(it) }

        viewModel.wind.observe(this) { weatherIconsImpl.setWindSpeed(it) }

        viewModel.desc.observe(this) {
            desc = it
            weatherIconsImpl.setDescription(it)
        }

        viewModel.time.observe(this) { MoreInfo.time = it }

        viewModel.fTemp.observe(this) { MoreInfo.fTemp = it }

        viewModel.fFeels.observe(this) { MoreInfo.fFeels = it }

        viewModel.fWind.observe(this) { MoreInfo.fWind = it }

        viewModel.fWindDir.observe(this) { MoreInfo.fWindDir = it }

        viewModel.fHumidity.observe(this) { MoreInfo.fHumidity = it }

        viewModel.fPressure.observe(this) { MoreInfo.fPressure = it }

        viewModel.isNight.observe(this) {
            isNight = it
            timeModeImpl.setTimeMode(it)
        }


    }

    // Business logic //

        private fun openSettings() =
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

        fun applyCity() {



        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

                isLocationApplied = (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)

            }


        @SuppressLint("MissingPermission")
        private fun applyLocationIfAllowed() {

            if (isLocationApplied) {

                    try {

                        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1,
                            1f,
                            locationListener)

                    } catch (e: Throwable) {}

                } else {

                    locationAvailabilityImpl.showDenyError()

                }
        }

        private fun secondInit() {



        }

    //define the listener
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            stateImpl.setState(States.CITY_APPLIED)
            stateImpl.setState(States.LOADING)

            selectedLon = ((location.longitude * 1000).roundToInt() / 1000f)
            selectedLat = ((location.latitude * 1000).roundToInt() / 1000f)

            searchMode = UserPreferences.SearchMode.GEO
            boardImpl.setUserLocationTitle()
            CoroutineScope(Main).launch { viewModel.load(this) }

        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}

