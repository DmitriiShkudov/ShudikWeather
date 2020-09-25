package com.example.shkudikweatherapp.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.client.HttpClient
import com.example.shkudikweatherapp.presenters.BtnChangeCityPresenter
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.views.Background
import com.example.shkudikweatherapp.views.BtnChangeCity
import com.example.shkudikweatherapp.views.CitiesList
import com.example.shkudikweatherapp.views.ShareButton
import com.example.shkudikweatherapp.views.icons.DescriptionIcon
import com.example.shkudikweatherapp.views.icons.HumidityIcon
import com.example.shkudikweatherapp.views.icons.TempIcon
import com.example.shkudikweatherapp.views.icons.WindIcon
import kotlinx.android.synthetic.main.activity_main.*


internal fun View.showKeyboard(context: Context) =

    (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
    showSoftInput(this, 0)

internal fun View.hideKeyboard(context: Context) =

    (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
    hideSoftInputFromWindow(this.windowToken, 0)

internal fun EditText.reformat() {

    val text = this.text.toString()
    this.setText("${text[0].toUpperCase()}${text.substring(1..text.length - 1)}")

}




class MainActivity : MvpAppCompatActivity(), BtnChangeCity, DescriptionIcon, HumidityIcon, TempIcon,
                     WindIcon, Background, CitiesList, ShareButton {


    companion object {

        const val CONNECTED = 1

    }



    @InjectPresenter
    lateinit var changeCityButtonPresenter: BtnChangeCityPresenter

    private val handler = Handler {

        when (it.what) {

            CONNECTED -> {

                badConnection.visibility = View.GONE

            }

            else -> badConnection.visibility = View.VISIBLE

        }

        true

    }

    override fun onResume() {

        super.onResume()



    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // make fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        HttpClient.handlerForCheck = this.handler
        HttpClient.launchCheckConnectionThread()

        WeatherProvider.context = applicationContext

        btnChangeCity.setOnClickListener {

            changeCityButtonPresenter.enteringCity()

            btnApplyCity.setOnClickListener {

                changeCityButtonPresenter.applyCity()
                changeCityButtonPresenter.executeAndShowResult(inputCity.text.toString())

            }

        }


    }

    override fun applyCity() {

        inputCity.reformat()
        inputCity.hideKeyboard(applicationContext)
        inputCity.clearFocus()
        inputCityFrame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        btnChangeCity.isClickable = true
        btnApplyCity.setOnClickListener(null)

    }

    override fun enteringCity() {

        inputCityFrame.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        inputCity.requestFocus()
        inputCity.setText("")
        inputCity.showKeyboard(applicationContext)
        btnChangeCity.isClickable = false

    }

    override fun showResult(success: Boolean) {

        Toast.makeText(applicationContext,
            if (success) "City was changed" else "Entered city is wrong!", Toast.LENGTH_SHORT).show()

        inputCity.background =
            if (success)
                ResourcesCompat.getDrawable(resources, R.drawable.back_city_input, null)
            else
                ResourcesCompat.getDrawable(resources, R.drawable.back_city_input_wrong, null)

    }

    override fun showDescription(description: String) =
        tvDescriptionIcon.setText(description)



    override fun showHumidity(humidity: Int) {

    }

    override fun showTemp(temp: Float) {

    }

    override fun showWind(wind: Float) {

    }

    override fun clearBack() {

    }

    override fun cloudyBack() {

    }

    override fun rainyBack() {

    }

    override fun clear() {

    }

    override fun cloudy() {

    }

    override fun rainy() {

    }

    override fun updateList(usersInput: String) {

    }

    override fun hideList(usersInput: String) {

    }

    override fun showShareWays() {

    }


}