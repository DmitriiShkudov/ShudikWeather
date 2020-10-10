package com.example.shkudikweatherapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.adapters.RvHelpAdapter
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.State
import com.example.shkudikweatherapp.states.WeatherState
import com.example.shkudikweatherapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep


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

    override fun onResume() {

        super.onResume()

        viewModel.load()

        setLocale()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WeatherProvider.context = applicationContext
        UserPreferences.context = applicationContext
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        with(WeatherProvider.selectedCity) {

            input_city.setText(this)
            viewModel.applyCity(this)

        }

        viewModel.update()

        btn_change_city.setOnClickListener { viewModel.changingCity() }

        btn_apply_city.setOnClickListener {

            input_city.reformat()
            viewModel.applyCity(input_city.text.toString())
            viewModel.load()

        }

        btn_settings.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java))}

        ///
        ///
        ///
        ///
        viewModel.state.observe(this, {

            when (it) {

                State.CHANGING_CITY -> {

                    Log.d("Debug", "In the changing")

                    btn_apply_city.isClickable = true
                    with(input_city) {

                        input_city_frame.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                        setText("")
                        requestFocus()
                        showKeyboard(applicationContext)

                    }

                    updateRv()
                    rvHelp.visibility = View.VISIBLE

                    root.setOnClickListener {

                        // cancellation
                        rvHelp.visibility = View.GONE
                        btn_apply_city.isClickable = false
                        btn_change_city.isClickable = true
                        input_city.clearFocus()
                        input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                        input_city.hideKeyboard(applicationContext)
                        input_city.setText(WeatherProvider.selectedCity)
                        main_background.setOnClickListener(null)

                    }

                }

                State.LOADING -> {

                    btn_change_city.isClickable = true
                    btn_apply_city.isClickable = false
                    input_city.clearFocus()
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    input_city.hideKeyboard(applicationContext)
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.VISIBLE
                    rvHelp.visibility = View.GONE

                }

                State.UPDATED -> {

                    input_city.background = ResourcesCompat.getDrawable(resources, R.drawable.back_city_input, null)
                    btn_change_city.isClickable = true
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.GONE

                }

                State.BAD_CONNECTION -> {

                    bad_connection.visibility = View.VISIBLE
                    cpv_loading.visibility = View.GONE

                }

                State.WRONG_CITY -> {

                    cpv_loading.visibility = View.GONE

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)
                    input_city.background = ResourcesCompat.getDrawable(resources, R.drawable.back_city_input_wrong, null)

                    "-".apply {

                        tvWind.text = this
                        tvHumidity.text = this
                        tvTemp.text = this

                    }

                }

            }

        })

        viewModel.weatherState.observe(this) {

            main_background.setImageDrawable(when (it) {

                WeatherState.CLEAR -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_clear, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_clear, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_clear, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.clear, null)
                }
                WeatherState.CLOUDY -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_cloudy, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_cloudy, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_cloudy, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.cloud, null)
                }
                WeatherState.LOW_CLOUDY -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_low_cloudy, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_low_cloudy, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_low_cloudy, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.low_cloud, null)
                }
                WeatherState.RAIN -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_rainy, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_rainy, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_rainy, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.rain, null)
                }
                WeatherState.HUMID -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_humid, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_humid, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_humid, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.humid, null)
                }
                WeatherState.SNOW -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.snow, null)
                }
                WeatherState.LOW_SNOW -> {

                    windIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null)
                    humidityIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null)
                    tempIcon.background = ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null)

                    ResourcesCompat.getDrawable(resources, R.drawable.low_snow, null)
                }

            })

        }

        viewModel.temp.observe(this) { tvTemp.text = it }

        viewModel.humidity.observe(this) { tvHumidity.text = it }

        viewModel.wind.observe(this) { tvWind.text = it }

        viewModel.desc.observe(this) { tvDescriptionIcon.text = it }

    }

    fun updateRv() {

        val adapter = RvHelpAdapter(this, WeatherProvider.helpList)
        rvHelp.adapter = adapter
        rvHelp.layoutManager = LinearLayoutManager(applicationContext)

    }

    fun setLocaleText(obj: View) {

        when (UserPreferences.language) {

            UserPreferences.Language.RUS -> when (obj) {

                text_temp -> text_temp.text = "Температура"
                text_wind -> text_wind.text = "Скорость ветра"
                text_humidity -> text_humidity.text = "Влажность"
                input_city -> input_city.hint = "Введите город"
                text_bad_connection -> text_bad_connection.text = "Плохое соединение..."

            }

            UserPreferences.Language.GER -> when (obj) {

                text_temp -> text_temp.text = "Temperatur"
                text_wind -> text_wind.text = "Windgeschwindigkeit"
                text_humidity -> text_humidity.text = "Feuchtigkeit"
                input_city -> input_city.hint = "Stadt betreten"
                text_bad_connection -> text_bad_connection.text = "Schlechte Verbindung..."

            }

            UserPreferences.Language.ENG -> when (obj) {

                text_temp -> text_temp.text = "Temperature"
                text_wind -> text_wind.text = "Wind speed"
                text_humidity -> text_humidity.text = "Humidity"
                input_city -> input_city.hint = "Enter city"
                text_bad_connection -> text_bad_connection.text = "Bad connection..."

            }

        }
    }

    fun setLocale() {

        setLocaleText(text_temp)
        setLocaleText(text_wind)
        setLocaleText(text_humidity)
        setLocaleText(input_city)
        setLocaleText(text_bad_connection)

    }

}