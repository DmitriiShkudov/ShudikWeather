package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.isLocationApplied
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isNight
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.presentation_layer.fragments.MoreInfo
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.StateImpl
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.*
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


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
        recyclerHelpImpl = RecyclerHelpImpl(this)
        timeModeImpl = TimeModeImpl(this)
        weatherIconsImpl = WeatherIconsImpl(this)
        stateImpl = StateImpl(this)

        if (!isLocationApplied)
            locationAvailabilityImpl.askPermission()

        if (searchMode == UserPreferences.SearchMode.CITY)
            boardImpl.setCity() else boardImpl.setUserLocationTitle()

        recyclerHelpImpl.update()
        stateImpl.setState(MainStates.LOADING)
        boardImpl.setOnEnterClickEvent()
        viewModel.update()

        // On Clicks //

        btn_change_city.setOnClickListener {

            stateImpl.setState(MainStates.CHANGING_CITY)

            if (isMoreInfoOpened)
                moreInfoImpl.detach()

        }

        btn_geo.setOnClickListener {

            stateImpl.setState(MainStates.CHANGING_CITY_CANCELLED)

            if (isMoreInfoOpened)
                moreInfoImpl.detach()

            viewModel.applyLocation(this)

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

                stateImpl.setState(MainStates.MORE_INFO)
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

    private fun openSettings() =
        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            isLocationApplied = (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)

        }

}

