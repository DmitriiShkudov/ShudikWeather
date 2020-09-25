package com.example.shkudikweatherapp.client

import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.shkudikweatherapp.activities.MainActivity.Companion.CONNECTED
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.presenters.BtnChangeCityPresenter
import com.example.shkudikweatherapp.presenters.BtnChangeCityPresenter.Companion.SUCCESS_UPDATE
import com.example.shkudikweatherapp.providers.WeatherProvider
import com.google.gson.Gson
import okhttp3.*
import java.lang.Thread.sleep
import java.net.Socket

object HttpClient : OkHttpClient() {

    var handlerForUpdate: Handler? = null
    var handlerForCheck: Handler? = null

    fun launchCheckConnectionThread() {

        val request = Request.Builder().
            url("http://api.openweathermap.org/data/2.5/weather?q=Brazilia&appid=6a8c6db6e5c6f3972d7ae682ae812b52&lang=en").
            build()

            Thread {

                while (true) {

                    var connected = false

                    try {

                        Thread {

                            sleep(2500)
                            if (!connected) this.handlerForCheck?.sendEmptyMessage(-1)

                        }.start()

                        this.newCall(request).execute()
                        connected = true
                        this.handlerForCheck?.sendEmptyMessage(CONNECTED)

                    } catch (e: Throwable) {

                        this.handlerForCheck?.sendEmptyMessage(-1)

                    }

                    sleep(3000)

                }

            }.start()

        }

    fun loadWeatherInfo(city: String) {

        val url = HttpUrl.Builder().
            scheme("http").
            addPathSegments("data/2.5/weather").
            host("api.openweathermap.org").
            port(80).
            addQueryParameter("q", city).
            addQueryParameter("appid", "6a8c6db6e5c6f3972d7ae682ae812b52").
            addQueryParameter("lang", "en").
            build()

        val request = Request.Builder().url(url).build()

        try {

            Thread {

                Log.d("URL", url.toString())

                val response = this.newCall(request).execute()

                val receivedWeatherJSON = response.body!!.string()

                Log.d("RECEIVED WEATHER INFO:", receivedWeatherJSON)


                // case when user entered wrong city ( obj == null )
                if (Gson().fromJson(receivedWeatherJSON, Weather::class.java).weather?.get(0) != null) {

                    val description = Gson().fromJson(receivedWeatherJSON, Weather::class.java).weather?.get(0)?.description
                    val temp = Gson().fromJson(receivedWeatherJSON, Weather::class.java).main.temp.toString()
                    val humidity = Gson().fromJson(receivedWeatherJSON, Weather::class.java).main.humidity.toString()
                    val wind = Gson().fromJson(receivedWeatherJSON, Weather::class.java).wind.speed.toString()

                    val hashMapToSend = hashMapOf(
                        "description" to description,
                        "temp" to temp,
                        "humidity" to humidity,
                        "wind" to wind)

                    val successMessage = Message.obtain(this.handlerForUpdate, SUCCESS_UPDATE, hashMapToSend)

                    this.handlerForUpdate?.sendMessage(successMessage)

                } else {

                    this.handlerForUpdate?.sendEmptyMessage(-1)

                }

            }.start()

        } catch (e: Throwable) {

            this.handlerForUpdate?.sendEmptyMessage(-1)

        }
    }

}