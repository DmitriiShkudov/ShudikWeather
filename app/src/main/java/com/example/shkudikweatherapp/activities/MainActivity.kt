package com.example.shkudikweatherapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.adapters.RvHelpAdapter
import com.example.shkudikweatherapp.providers.Helper.EMPTY_INPUT_ERROR
import com.example.shkudikweatherapp.providers.Helper.LOW_ENG
import com.example.shkudikweatherapp.providers.Helper.LOW_GER
import com.example.shkudikweatherapp.providers.Helper.LOW_RUS
import com.example.shkudikweatherapp.providers.Helper.OVERCAST_ENG
import com.example.shkudikweatherapp.providers.Helper.OVERCAST_GER
import com.example.shkudikweatherapp.providers.Helper.OVERCAST_RUS
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.MainDescription
import com.example.shkudikweatherapp.states.State
import com.example.shkudikweatherapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


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

    private lateinit var viewModel: MainViewModel

    private var city: String
    get() = WeatherProvider.selectedCity
    set(value) {
        WeatherProvider.selectedCity = value
    }

    override fun onResume() {

        super.onResume()
        viewModel.load()
        setLocale()

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        WeatherProvider.context = applicationContext
        UserPreferences.context = applicationContext
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if (UserPreferences.fullscreen) setTheme(R.style.fullscreen)

        //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //

        // start action
        input_city.setText(city)
        viewModel.stateLoading()
        viewModel.update()

        btn_change_city.setOnClickListener {

            UserPreferences.searchMode = UserPreferences.SearchMode.CITY
            setEnabledSearchMode(viewModel.isNight.value!!)
            viewModel.stateChangingCity()

        }

        btn_geo.setOnClickListener {

            UserPreferences.searchMode = UserPreferences.SearchMode.GEO
            setEnabledSearchMode(viewModel.isNight.value!!)
            viewModel.stateChangingCity()

        }

        btn_apply_city.setOnClickListener {

            if ( input_city.text.isNotEmpty() ) {

                input_city.reformat()
                city = input_city.text.toString()
                viewModel.stateCityApplied()
                viewModel.stateLoading()
                viewModel.load()

            } else {

                Toast.makeText(applicationContext, EMPTY_INPUT_ERROR, Toast.LENGTH_LONG).show()

            }


        }

        btn_settings.setOnClickListener {

            startActivity(Intent(this, SettingsActivity::class.java))

        }

        // observers //

        viewModel.state.observe(this, {

            when (it) {

                State.CHANGING_CITY -> {

                    tvDescriptionIcon.visibility = View.INVISIBLE
                    updateRv()
                    rvHelp.visibility = View.VISIBLE
                    btn_apply_city.isClickable = true

                    with(input_city) {

                        input_city_frame.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                        setText("")
                        requestFocus()
                        showKeyboard(applicationContext)

                    }

                    root.setOnClickListener {

                        viewModel.stateChangingCityCancelled()

                    }

                }

                State.CHANGING_CITY_CANCELLED -> {

                    // cancellation
                    rvHelp.visibility = View.GONE
                    btn_apply_city.isClickable = false
                    btn_change_city.isClickable = true
                    input_city.clearFocus()
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    input_city.hideKeyboard(applicationContext)
                    input_city.setText(WeatherProvider.selectedCity)
                    main_background.setOnClickListener(null)
                    tvDescriptionIcon.visibility = View.VISIBLE

                }

                State.LOADING -> {

                    input_city.background = ResourcesCompat.getDrawable(resources, R.drawable.input_empty, null)
                    btn_change_city.isClickable = true
                    btn_apply_city.isClickable = false
                    input_city.clearFocus()
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    input_city.hideKeyboard(applicationContext)
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.VISIBLE
                    rvHelp.visibility = View.GONE
                }

                State.CITY_APPLIED -> {

                    tvDescriptionIcon.visibility = View.VISIBLE

                }

                State.UPDATED -> {

                    btn_change_city.isClickable = true
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.GONE
                    setEnabledSearchMode(viewModel.isNight.value!!)

                }

                State.BAD_CONNECTION -> {

                    bad_connection.visibility = View.VISIBLE
                    cpv_loading.visibility = View.GONE

                }

                State.WRONG_CITY -> {

                    cpv_loading.visibility = View.GONE
                    bad_connection.visibility = View.GONE
                    tvDescriptionIcon.visibility = View.VISIBLE


                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)


                    input_city.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.back_city_input_wrong,
                        null
                    )

                    "-".also {

                        tvWind.text = it
                        tvHumidity.text = it
                        tvTemp.text = it

                    }

                }

            }

        })


        viewModel.mainDesc.observe(this) {

            main_background.setImageDrawable(

                when (it) {

                    MainDescription.CLEAR -> {

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_clear,
                            null
                        )

                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_clear,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_clear,
                            null
                        )

                        dayMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.clear, null)
                    }

                    MainDescription.CLEAR_NIGHT -> {

                        nightMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.clear_night, null)

                    }

                    MainDescription.CLOUDS -> {

                        val isOvercast = viewModel.desc.value!!.contains(OVERCAST_ENG) ||
                                         viewModel.desc.value!!.contains(OVERCAST_RUS) ||
                                         viewModel.desc.value!!.contains(OVERCAST_GER)

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_cloudy,
                            null
                        )

                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_cloudy,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_cloudy,
                            null
                        )

                        dayMode()
                        ResourcesCompat.getDrawable(resources, if (isOvercast) R.drawable.cloud else R.drawable.low_cloud, null)
                    }

                    MainDescription.CLOUDS_NIGHT -> {

                        val isOvercast = viewModel.desc.value!!.contains(OVERCAST_ENG) ||
                                viewModel.desc.value!!.contains(OVERCAST_RUS)

                        nightMode()
                        ResourcesCompat.getDrawable(resources, if (isOvercast) R.drawable.cloud_night else R.drawable.low_cloud_night, null)

                    }

                    MainDescription.RAIN -> {

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_rainy,
                            null
                        )

                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_rainy,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_rainy,
                            null
                        )

                        dayMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.rain, null)
                    }

                    MainDescription.RAIN_NIGHT -> {

                        nightMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.rain_night, null)

                    }

                    MainDescription.HAZE, MainDescription.MIST, MainDescription.DUST,
                    MainDescription.FOG, MainDescription.SMOKE -> {

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_humid,
                            null
                        )
                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_humid,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_humid,
                            null
                        )

                        dayMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.humid, null)
                    }

                    MainDescription.HAZE_NIGHT, MainDescription.MIST_NIGHT, MainDescription.DUST_NIGHT,
                    MainDescription.FOG_NIGHT, MainDescription.SMOKE_NIGHT -> {

                        nightMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)

                    }

                    MainDescription.SNOW -> {

                        val isLow = viewModel.desc.value!!.contains(LOW_ENG) ||
                                viewModel.desc.value!!.contains(LOW_RUS) ||
                                viewModel.desc.value!!.contains(LOW_GER)

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_snow,
                            null
                        )
                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_snow,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_snow,
                            null
                        )

                        dayMode()
                        ResourcesCompat.getDrawable(resources, if ( isLow ) R.drawable.low_snow else R.drawable.snow, null)
                    }

                    MainDescription.SNOW_NIGHT -> {

                        nightMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.snow_night, null)

                    }

                    else -> {
                        nightMode()
                        ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)
                    }

                }
            )

        }

        viewModel.temp.observe(this) { tvTemp.text = it }

        viewModel.humidity.observe(this) { tvHumidity.text = it }

        viewModel.wind.observe(this) { tvWind.text = it }

        viewModel.desc.observe(this) { tvDescriptionIcon.text = it }

    }

    private fun nightMode() {

        btn_share.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.share_night, null))
        btn_change_city.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.replace_night, null))
        btn_settings.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.settings_night, null))
        btn_apply_city.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.apply_city_night, null))

        ResourcesCompat.getColor(resources, R.color.white, null).also {

            tvDescriptionIcon.setTextColor(it)
            input_city.setTextColor(it)
            tvTemp.setTextColor(it)
            tvHumidity.setTextColor(it)
            tvWind.setTextColor(it)
            text_humidity.setTextColor(it)
            text_wind.setTextColor(it)
            text_temp.setTextColor(it)

        }

        tempIcon.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.back_icons_night,
            null
        )

        windIcon.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.back_icons_night,
            null
        )
        humidityIcon.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.back_icons_night,
            null
        )

        input_city.background = ResourcesCompat.getDrawable(resources, R.drawable.back_city_input_night, null)

    }

    private fun dayMode() {

        btn_share.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.share, null))
        btn_apply_city.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.apply_city, null))
        btn_change_city.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.replace, null))
        btn_settings.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.settings, null))

        ResourcesCompat.getColor(resources, R.color.black, null).also {

            tvDescriptionIcon.setTextColor(it)
            input_city.setTextColor(it)
            tvTemp.setTextColor(it)
            tvHumidity.setTextColor(it)
            tvWind.setTextColor(it)
            text_humidity.setTextColor(it)
            text_wind.setTextColor(it)
            text_temp.setTextColor(it)

        }

        input_city.background = ResourcesCompat.getDrawable(resources, R.drawable.back_city_input, null)

    }

    fun updateRv() {

        val adapter = RvHelpAdapter(this, WeatherProvider.helpList)
        rvHelp.adapter = adapter
        rvHelp.layoutManager = LinearLayoutManager(applicationContext)

    }

    private fun setLocaleText(obj: View) {

        when (UserPreferences.language) {

            UserPreferences.Language.RUS -> when (obj) {

                text_temp -> text_temp.text = "Температура"
                text_wind -> text_wind.also { it.textSize = 16f }.text = "Скорость ветра"
                text_humidity -> text_humidity.text = "Влажность"
                input_city -> input_city.hint = "Введите город"
                text_bad_connection -> text_bad_connection.text = "Плохое соединение..."

            }

            UserPreferences.Language.GER -> when (obj) {

                text_temp -> text_temp.text = "Temperatur"
                text_wind -> text_wind.also { it.textSize = 13f }.text = "Windgeschwindigkeit"
                text_humidity -> text_humidity.text = "Feuchtigkeit"
                input_city -> input_city.hint = "Stadt betreten"
                text_bad_connection -> text_bad_connection.text = "Schlechte Verbindung..."

            }

            UserPreferences.Language.ENG -> when (obj) {

                text_temp -> text_temp.text = "Temperature"
                text_wind -> text_wind.also { it.textSize = 16f }.text = "Wind speed"
                text_humidity -> text_humidity.text = "Humidity"
                input_city -> input_city.hint = "Enter city"
                text_bad_connection -> text_bad_connection.text = "Bad connection..."

            }

        }
    }

    private fun setLocale() {

        setLocaleText(text_temp)
        setLocaleText(text_wind)
        setLocaleText(text_humidity)
        setLocaleText(input_city)
        setLocaleText(text_bad_connection)

    }

    private fun setEnabledSearchMode(isNight: Boolean) {

        when (UserPreferences.searchMode) {

            UserPreferences.SearchMode.CITY -> {

                btn_change_city.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    if (isNight) R.drawable.replace_enabled_night else R.drawable.replace_enabled, null))

                btn_geo.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    if (isNight) R.drawable.location_night else R.drawable.location, null))

            }
            UserPreferences.SearchMode.GEO -> {

                btn_change_city.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    if (isNight) R.drawable.replace_night else R.drawable.replace, null))

                btn_geo.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    if (isNight) R.drawable.location_enabled_night else R.drawable.location_enabled, null))

            }

        }

    }

}

