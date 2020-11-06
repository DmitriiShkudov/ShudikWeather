package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.value
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.isLocationApplied
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isNight
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.presentation_layer.fragments.MoreInfo
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStateImpl
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
        const val REQUEST_PERMISSION_CODE = 44


    }

    lateinit var viewModel: MainViewModel

    //
    internal val backgroundImpl = BackgroundImpl(this)
    internal val boardImpl = BoardImpl(this)
    internal val changeableSearchModeImpl = ChangeableSearchModeImpl(this)
    internal val moreInfoImpl = MoreInfoImpl(this)
    internal val localeImpl =  LocaleImpl(this)
    internal val locationAvailabilityImpl = LocationAvailabilityImpl(this)
    internal val recyclerHelpImpl = RecyclerHelpImpl(this)
    internal val timeModeImpl = TimeModeImpl(this)
    internal val weatherIconsImpl = WeatherIconsImpl(this)
    internal val stateImpl = MainStateImpl(this)
    internal val iconMoreInfoImpl = IconMoreInfoImpl(this)
    //



    override fun onResume() {

        super.onResume()

        stateImpl.setState(MainStates.UPDATED)
        CoroutineScope(Main).launch { viewModel.load(this) }

    }


    override fun onStart() {
        super.onStart()

        btn_change_city.setOnClickListener {

            if (viewModel.state.value == MainStates.MORE_INFO)
                moreInfoImpl.detach()

            viewModel.state.value(MainStates.CHANGING_CITY)

        }

        btn_geo.setOnClickListener {

            if (viewModel.state.value == MainStates.MORE_INFO)
                moreInfoImpl.detach()

            viewModel.applyLocation(this)

            viewModel.state.value(MainStates.CHANGING_CITY_CANCELLED)

        }

        btn_apply_city.setOnClickListener {

            viewModel.applyCity(this)

        }

        btn_settings.setOnClickListener {

            if (viewModel.state.value == MainStates.MORE_INFO)
                moreInfoImpl.detach()

            openSettings()

        }

        tvDescriptionIcon.setOnClickListener {

            if (isSelectedCityExists) {

                viewModel.state.value(MainStates.MORE_INFO)

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // init before drawing the interface

        WeatherProvider.context = applicationContext
        UserPreferences.context = applicationContext
        Helper.context = applicationContext
        viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        if (UserPreferences.fullscreen) setTheme(R.style.fullscreen)

        //

        // drawing interface

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //

        // init

        boardImpl.setOnEnterClickEvent()
        locationAvailabilityImpl.askPermission(!isLocationApplied)

        stateImpl.setState(MainStates.UPDATED)
        stateImpl.setState(MainStates.LOADING)
        viewModel.update()

        //

        // Reactions

        viewModel.state.observe(this) { stateImpl.setState(it) }

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
        startActivity(Intent(this@MainActivity, SettingsActivity::class.java).apply {

            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        })

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

