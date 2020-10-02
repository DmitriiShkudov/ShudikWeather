package com.example.shkudikweatherapp.threads

import android.os.Handler
import com.example.shkudikweatherapp.client.HttpClient
import com.example.shkudikweatherapp.providers.WeatherProvider
import okhttp3.internal.wait

object ThreadManager {

    private var weatherThreadRef: Thread? = null
    private var connectionThreadRef: Thread? = null

    var handler: Handler? = null

    fun startConnectionThread() {

        this.connectionThreadRef = Thread {

            try {

                while (true) {

                    HttpClient.checkConnection(this.handler!!)
                    Thread.sleep(3000)

                }

            } catch (e: InterruptedException) {}

        }

        this.connectionThreadRef?.start()

    }


    fun restartWeatherThread() {

        this.breakWeatherThread()

        this.weatherThreadRef = Thread {

            try {

                while (true) {

                    HttpClient.loadWeatherInfo(WeatherProvider.selectedCity, this.handler!!)
                    Thread.sleep(10000)

                }

            } catch (e: InterruptedException) {}

        }

        this.weatherThreadRef?.start()


    }

    fun restartWeatherThreadIfBroken() {

        if (this.weatherThreadRef == null) return

        if (!this.weatherThreadRef!!.isAlive) {

            this.weatherThreadRef = Thread {

                try {

                    while (true) {

                        HttpClient.loadWeatherInfo(WeatherProvider.selectedCity, this.handler!!)
                        Thread.sleep(3000)

                    }

                } catch (e: InterruptedException) { }

            }

            this.weatherThreadRef?.start()
        }


    }

    fun breakWeatherThread() = this.weatherThreadRef?.interrupt()






}