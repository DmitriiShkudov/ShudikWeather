package com.example.shkudikweatherapp.client

import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.shkudikweatherapp.activities.MainActivity
import com.example.shkudikweatherapp.activities.MainActivity.Companion.CONNECTED
import com.example.shkudikweatherapp.pojo.weather.Weather
import com.example.shkudikweatherapp.presenters.main_activity.BtnChangeCityPresenter
import com.google.gson.Gson
import okhttp3.*
import java.lang.Thread.sleep

object HttpClient : OkHttpClient() {

    fun checkConnection(handler: Handler) {

        val request = Request.Builder().
            url("http://api.openweathermap.org/data/2.5/weather?q=Brazilia&appid=6a8c6db6e5c6f3972d7ae682ae812b52&lang=en").
            build()

            Thread {

                var connected = false

                Thread {

                    sleep(2500)
                    if (!connected) handler.sendEmptyMessage(-1)

                }.start()

                try {

                    this.newCall(request).execute()

                } catch(e: Throwable) {}

                connected = true

                handler.sendEmptyMessage(CONNECTED)

            }.start()

        }

    fun checkCity(city: String, handler: Handler) {

        val url = HttpUrl.Builder().
            scheme("http").
            addPathSegments("data/2.5/weather").
            host("api.openweathermap.org").
            port(80).
            addQueryParameter("q", city).
            addQueryParameter("appid", "6a8c6db6e5c6f3972d7ae682ae812b52").
            addQueryParameter("lang", "ru").
            build()

        val request = Request.Builder().url(url).build()

            Thread {

                Log.d("URL", url.toString())

                try {
                    val response = this.newCall(request).execute()
                    val receivedWeatherJSON = response.body!!.string()

                    Log.d("RECEIVED WEATHER INFO:", receivedWeatherJSON)

                    // case when user entered wrong city ( obj == null )
                    if (Gson().fromJson(receivedWeatherJSON, Weather::class.java).weather?.get(0) != null) {

                        handler.sendEmptyMessage(BtnChangeCityPresenter.UPDATE_CITY)

                    } else {

                        handler.sendEmptyMessage(-1)

                    }

                } catch(e: Throwable) {}

            }.start()

    }

    fun loadWeatherInfo(city: String, handler: Handler) {

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

            Thread {

                Log.d("URL", url.toString())

                try {

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

                        val successMessage = Message.obtain(handler, MainActivity.UPDATE, hashMapToSend)

                        handler.sendMessage(successMessage)

                    }

                } catch (e: Throwable) {}

            }.start()


    }

}