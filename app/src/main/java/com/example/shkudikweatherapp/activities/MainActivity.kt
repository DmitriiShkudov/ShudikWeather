package com.example.shkudikweatherapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.adapters.RvHelpAdapter
import com.example.shkudikweatherapp.client.HttpClient
import com.example.shkudikweatherapp.presenters.main_activity.BackgroundPresenter
import com.example.shkudikweatherapp.presenters.main_activity.BtnChangeCityPresenter
import com.example.shkudikweatherapp.presenters.main_activity.icons.DescriptionIconPresenter
import com.example.shkudikweatherapp.presenters.main_activity.icons.HumidityIconPresenter
import com.example.shkudikweatherapp.presenters.main_activity.icons.TempIconPresenter
import com.example.shkudikweatherapp.presenters.main_activity.icons.WindIconPresenter
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.threads.ThreadManager
import com.example.shkudikweatherapp.views.main_activity.*
import com.example.shkudikweatherapp.weather_states.Background
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


class MainActivity : MvpAppCompatActivity(), BtnChangeCity, DescriptionIcon, HumidityIcon, TempIcon,
    WindIcon, Background, ShareButton {


    companion object {

        const val CONNECTED = 1
        const val UPDATE = 2

    }

    @InjectPresenter
    lateinit var btnChangeCityPresenter: BtnChangeCityPresenter

    @InjectPresenter
    lateinit var descriptionPresenter: DescriptionIconPresenter

    @InjectPresenter
    lateinit var humidityPresenter: HumidityIconPresenter

    @InjectPresenter
    lateinit var tempPresenter: TempIconPresenter

    @InjectPresenter
    lateinit var windPresenter: WindIconPresenter

    @InjectPresenter
    lateinit var backgroundPresenter: BackgroundPresenter

    private var rvAdapter: RvHelpAdapter? = null

    private val handler = Handler {

        when (it.what) {

            UPDATE -> {

                val receivedWeatherInfo = it.obj as HashMap<*, *>

                WeatherProvider.temperature =
                    (receivedWeatherInfo["temp"] as String).toFloat().toInt()
                WeatherProvider.description = receivedWeatherInfo["description"] as String
                WeatherProvider.humidity = (receivedWeatherInfo["humidity"] as String).toInt()
                WeatherProvider.wind = (receivedWeatherInfo["wind"] as String).toFloat().toInt()

                descriptionPresenter.showDescription()
                humidityPresenter.showHumidity()
                tempPresenter.showTemp()
                windPresenter.showWind()

                backgroundPresenter.set()

            }

            CONNECTED -> {

                badConnection.visibility = View.GONE
                ThreadManager.restartWeatherThreadIfBroken()

            }

            else -> {

                badConnection.visibility = View.VISIBLE
                ThreadManager.breakWeatherThread()

            }

        }

        true

    }

    init {

        ThreadManager.handler = this.handler

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init
        WeatherProvider.context = applicationContext

        UserPreferences.context = applicationContext

        if (UserPreferences.fullscreen) {

            window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE)


        }

        inputCity.setText(WeatherProvider.selectedCity)

        this.updateRv()

        ThreadManager.startConnectionThread()

        HttpClient.checkCity(WeatherProvider.selectedCity, this.handler)

        ThreadManager.restartWeatherThread()

        btnChangeCity.setOnClickListener {

            btnChangeCityPresenter.enteringCity()

            btnApplyCity.setOnClickListener {

                val enteredCity = inputCity.text.toString()

                if (enteredCity.isNotEmpty()) {

                    btnChangeCityPresenter.applyCity()
                    btnChangeCityPresenter.executeAndShowResult(enteredCity)

                } else Toast.makeText(applicationContext, "Enter a city name!", Toast.LENGTH_LONG).show()

            }

        }

        btnSettings.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }


    }

    override fun applyCity() {

        rvHelp.visibility = View.GONE
        inputCity.reformat()
        inputCity.hideKeyboard(applicationContext)
        inputCity.clearFocus()
        inputCityFrame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        btnChangeCity.isClickable = true
        btnApplyCity.setOnClickListener(null)


    }

    override fun enteringCity() {

        rvHelp.visibility = View.VISIBLE
        inputCityFrame.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        inputCity.requestFocus()
        inputCity.setText("")
        inputCity.showKeyboard(applicationContext)
        btnChangeCity.isClickable = false

        root.setOnClickListener { cancel() }

    }

    override fun cancel() {

        inputCity.setText(WeatherProvider.selectedCity)
        rvHelp.visibility = View.GONE
        inputCity.hideKeyboard(applicationContext)
        inputCity.clearFocus()
        inputCityFrame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        btnChangeCity.isClickable = true
        btnApplyCity.setOnClickListener(null)
        root.setOnClickListener(null)

    }

    override fun showResult(success: Boolean) {

        if (success) {

            updateRv()

            inputCity.background =
                ResourcesCompat.getDrawable(resources, R.drawable.back_city_input, null)

        } else {

            Log.d("Debug", "Holy shit's")

            inputCity.background =
                ResourcesCompat.getDrawable(resources, R.drawable.back_city_input_wrong, null)

            ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null).apply {

                tempIcon.background = this
                humidityIcon.background = this
                windIcon.background = this

            }

            tvDescriptionIcon.text = "This city isn't exist!"
            tvTemp.text = "-"
            tvHumidity.text = "-"
            tvWind.text = "-"

        }

    }

    override fun showDescription(description: String) =
        tvDescriptionIcon.setText(description)


    override fun showHumidity(humidity: Int) =
        tvHumidity.setText("${humidity}${"%"}")


    override fun showTemp(temp: Int) =
        tvTemp.setText("${temp - 273}${"°С"}")


    override fun showWind(wind: Int) =
        tvWind.setText("${wind}${" m/s"}")

    override fun lowSnow() {
        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.low_snow,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }
    }

    override fun snow() {

        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.low_snow,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_snow, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }

    }


    override fun clear() {
        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.clear,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_clear, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }

    }

    override fun cloudy() {
        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.cloud,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_cloudy, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }

    }

    override fun lowCloudy() {
        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.low_cloud,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_low_cloudy, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }
    }

    override fun rainy() {
        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.rain,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_rainy, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }

    }

    override fun humid() {
        mainBackground.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.humid,
                null
            )
        )

        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_humid, null).apply {

            tempIcon.background = this
            humidityIcon.background = this
            windIcon.background = this

        }

    }


    override fun showShareWays() {

    }


    fun fillTheInput(value: String) = inputCity.setText(value)

    fun updateRv() {

        rvAdapter = RvHelpAdapter(this, WeatherProvider.helpList)
        rvAdapter!!.context = applicationContext
        rvHelp.adapter = rvAdapter
        rvHelp.layoutManager = LinearLayoutManager(applicationContext)

    }

}