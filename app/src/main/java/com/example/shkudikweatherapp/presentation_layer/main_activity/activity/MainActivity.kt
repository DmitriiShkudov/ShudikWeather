package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import com.example.shkudikweatherapp.presentation_layer.fragments.MoreInfo
import com.example.shkudikweatherapp.presentation_layer.main_activity.views.*
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.language
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isNight
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedCity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedLat
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedLon
import com.example.shkudikweatherapp.presentation_layer.states.States
import com.example.shkudikweatherapp.presentation_layer.viewmodels.MainViewModel
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY_INPUT_ERROR_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY_INPUT_ERROR_GER
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY_INPUT_ERROR_RUS
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.isLocationApplied
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
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

    private lateinit var viewModel: MainViewModel

    private lateinit var backgroundImpl: BackgroundImpl
    private lateinit var boardImpl: BoardImpl
    private lateinit var changebleSearchModeImpl: ChangebleSearchModeImpl
    private lateinit var moreInfoImpl: MoreInfoImpl
    private lateinit var localeImpl: LocaleImpl
    private lateinit var locationAvailabilityImpl: LocationAvailabilityImpl
    private lateinit var recyclerHelpImpl: RecyclerHelpImpl
    private lateinit var stateImpl: StateImpl
    private lateinit var timeModeImpl: TimeModeImpl
    private lateinit var weatherIconsImpl: WeatherIconsImpl



    override fun onResume() {

        super.onResume()
        viewModel.load()
        localeImpl.setLocale()

        if (searchMode == UserPreferences.SearchMode.GEO) {

            boardImpl.setUserLocationTitle()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        firstInit()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backgroundImpl = BackgroundImpl(this)
        boardImpl = BoardImpl(this)
        changebleSearchModeImpl = ChangebleSearchModeImpl(this)
        moreInfoImpl = MoreInfoImpl(this)
        localeImpl =  LocaleImpl(this)
        locationAvailabilityImpl = LocationAvailabilityImpl(this)
        recyclerHelpImpl = RecyclerHelpImpl(this, boardImpl)
        timeModeImpl = TimeModeImpl(this)
        weatherIconsImpl = WeatherIconsImpl(this)
        stateImpl = StateImpl(this, moreInfoImpl, recyclerHelpImpl, changebleSearchModeImpl, timeModeImpl)

        secondInit()

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

            applyCityIfEntered()

        }

        btn_settings.setOnClickListener {

            if (isMoreInfoOpened)
                moreInfoImpl.detach()

            openSettings()

        }

        btn_share.setOnClickListener {

            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Димтри")

                // (Optional) Here we're setting the title of the content
                putExtra(Intent.EXTRA_TITLE, "Introducing content previews")

                type = "text/plain"

                // (Optional) Here we're passing a content URI to an image to be displayed
                data = Uri.parse("34 градуса!")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }, null)
            startActivity(share)

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

        private fun openSettings() = startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

        private fun applyCityIfEntered() {

            if (input_city.text.isNotEmpty()) {

                searchMode = UserPreferences.SearchMode.CITY

                input_city.reformat()
                selectedCity = input_city.text.toString()
                stateImpl.setState(States.CITY_APPLIED)
                stateImpl.setState(States.LOADING)

                viewModel.load()

            } else {

                Toast.makeText(applicationContext, when (language) {

                    UserPreferences.Language.RUS -> EMPTY_INPUT_ERROR_RUS
                    UserPreferences.Language.ENG -> EMPTY_INPUT_ERROR_ENG
                    UserPreferences.Language.GER -> EMPTY_INPUT_ERROR_GER

                }, Toast.LENGTH_LONG).show()

            }

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


        private fun applyLocationIfAllowed() {

            if (isLocationApplied) {

                    searchMode = UserPreferences.SearchMode.GEO

                    val locationProvider =
                        LocationServices.getFusedLocationProviderClient(this@MainActivity)

                    locationProvider.lastLocation.addOnCompleteListener {

                        try {

                            stateImpl.setState(States.CITY_APPLIED)
                            stateImpl.setState(States.LOADING)

                            selectedLon = ((it.result.longitude * 1000).roundToInt() / 1000f)
                            selectedLat = ((it.result.latitude * 1000).roundToInt() / 1000f)

                            // my case
                            selectedLon = 37.344f
                            selectedLat = 55.841f

                            boardImpl.setUserLocationTitle()

                            viewModel.load()

                        } catch (e: Throwable) {}

                    }

                } else {

                    locationAvailabilityImpl.showDenyError()

                }

        }

        private fun firstInit() {

            WeatherProvider.context = applicationContext
            UserPreferences.context = applicationContext
            viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
            if (UserPreferences.fullscreen) setTheme(R.style.fullscreen)

        }

        private fun secondInit() {

            if (!isLocationApplied)
                locationAvailabilityImpl.askPermission()

            if (searchMode == UserPreferences.SearchMode.CITY)
                boardImpl.setCity() else boardImpl.setUserLocationTitle()

            recyclerHelpImpl.update()
            stateImpl.setState(States.LOADING)
            viewModel.update()

        }

}

