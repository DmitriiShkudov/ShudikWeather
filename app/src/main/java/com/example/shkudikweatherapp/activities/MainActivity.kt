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
import com.example.shkudikweatherapp.http_client.RetrofitClient
import com.example.shkudikweatherapp.http_client.WeatherService
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.presenters.main_activity.BackgroundPresenter
import com.example.shkudikweatherapp.presenters.main_activity.BtnChangeCityPresenter
import com.example.shkudikweatherapp.presenters.main_activity.icons.*
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.example.shkudikweatherapp.states.State
import com.example.shkudikweatherapp.viewmodels.MainViewModel
import com.example.shkudikweatherapp.views.main_activity.*
import com.example.shkudikweatherapp.weather_states.Background
import kotlinx.android.synthetic.main.activity_main.*
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.state.observe(this, {



        })

    }



}