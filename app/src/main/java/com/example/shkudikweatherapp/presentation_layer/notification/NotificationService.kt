package com.example.shkudikweatherapp.presentation_layer.notification

import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import com.example.shkudikweatherapp.data_layer.http_client.NotificationServiceRetrofitClient
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.getMainDescription
import com.example.shkudikweatherapp.data_layer.providers.Helper.isNightTime
import com.example.shkudikweatherapp.data_layer.providers.Helper.setTemp
import com.example.shkudikweatherapp.data_layer.providers.Helper.setWindDirection
import com.example.shkudikweatherapp.data_layer.providers.Helper.setWindSpeed
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.notificationCity
import com.example.shkudikweatherapp.data_layer.enums.MainDescription.*
import com.example.shkudikweatherapp.data_layer.providers.Helper.NOTIFICATION_CHANNEL
import com.example.shkudikweatherapp.data_layer.providers.Helper.Objects.direction
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.squareup.picasso.Picasso
import java.lang.Thread.sleep


class NotificationService : Service() {

    private val retrofitClient = NotificationServiceRetrofitClient()

    override fun onCreate() {
        super.onCreate()

            Thread {
                while (true) {
                    load()
                    sleep(15000)
                }
            }.start()

        }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private fun makeNotification(temp: String,
                                 mainDesc: MainDescription,
                                 desc: String,
                                 humidity: String,
                                 wind: String,
                                 windDir: String,
                                 time: String): Notification {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nChannel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_LOW).apply {

                setSound(null, null)

            }
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(nChannel)

        }

        val bitmap = Picasso.with(applicationContext)
            .load("http://openweathermap.org/img/wn/${mainDesc.icon}@2x.png").get()

        val remoteView = RemoteViews(packageName, R.layout.notification_layout).apply {

            setImageViewBitmap(
                R.id.imgBackground, BitmapFactory.decodeResource(applicationContext.resources,

                when (mainDesc) {

                    CLEAR -> R.drawable.clear

                    CLEAR_NIGHT -> R.drawable.clear_night

                    CLOUDS -> {

                        val isOvercast = desc.contains(getString(R.string.overcast_rus)) ||
                                         desc.contains(getString(R.string.overcast_eng)) ||
                                         desc.contains(getString(R.string.overcast_ger))

                        if (isOvercast) R.drawable.cloud else R.drawable.low_cloud
                    }

                    CLOUDS_NIGHT -> {

                        val isOvercast = desc.contains(getString(R.string.overcast_rus)) ||
                                         desc.contains(getString(R.string.overcast_eng)) ||
                                         desc.contains(getString(R.string.overcast_ger))

                        if (isOvercast) R.drawable.cloud_night else R.drawable.low_cloud_night

                    }

                    RAIN -> R.drawable.rain

                    RAIN_NIGHT -> R.drawable.rain_night

                    HAZE, MIST, DUST, FOG, SMOKE -> R.drawable.humid

                    HAZE_NIGHT, MIST_NIGHT, DUST_NIGHT, FOG_NIGHT, SMOKE_NIGHT -> R.drawable.humid_night

                    THUNDERSTORM_NIGHT, THUNDERSTORM -> R.drawable.thunder

                    TORNADO_NIGHT, TORNADO -> R.drawable.tornado

                    SNOW -> {

                        val isLow = desc.contains(getString(R.string.low_rus)) ||
                                    desc.contains(getString(R.string.low_eng)) ||
                                    desc.contains(getString(R.string.low_ger))

                        if (isLow) R.drawable.low_snow else R.drawable.snow

                    }

                    SNOW_NIGHT -> R.drawable.snow_night

                    else -> R.drawable.humid_night


                }))

            if (isNightTime(time)) {

                if (mainDesc != CLOUDS_NIGHT &&
                    mainDesc != RAIN_NIGHT &&
                    mainDesc != THUNDERSTORM_NIGHT &&
                    mainDesc != THUNDERSTORM) {

                    setTextColor(
                        R.id.firstInfo, ResourcesCompat.getColor(resources,
                            R.color.white, null))
                    setTextColor(
                        R.id.secondInfo, ResourcesCompat.getColor(resources,
                            R.color.white, null))
                    setTextColor(
                        R.id.thirdInfo, ResourcesCompat.getColor(resources,
                            R.color.white, null))
                    setTextColor(
                        R.id.tvTitle, ResourcesCompat.getColor(resources,
                            R.color.bright_white, null))

                } else {

                    setTextColor(
                        R.id.firstInfo, ResourcesCompat.getColor(resources,
                            R.color.black, null))
                    setTextColor(
                        R.id.secondInfo, ResourcesCompat.getColor(resources,
                            R.color.black, null))
                    setTextColor(
                        R.id.thirdInfo, ResourcesCompat.getColor(resources,
                            R.color.black, null))
                    setTextColor(
                        R.id.tvTitle, ResourcesCompat.getColor(resources,
                            R.color.black, null))

                }

            } else {

                setTextColor(R.id.firstInfo, ResourcesCompat.getColor(resources, R.color.black, null))
                setTextColor(
                    R.id.secondInfo, ResourcesCompat.getColor(resources,
                        R.color.black, null))
                setTextColor(R.id.thirdInfo, ResourcesCompat.getColor(resources, R.color.black, null))
                setTextColor(R.id.tvTitle, ResourcesCompat.getColor(resources, R.color.black, null))

            }

            setImageViewBitmap(R.id.iconImg, bitmap)
            setTextViewText(R.id.firstInfo, "$temp, $desc")
            setTextViewText(R.id.secondInfo, "${Helper.Objects.wind} $wind, $direction $windDir")
            setTextViewText(R.id.thirdInfo, "${Helper.Objects.humidity} $humidity")
            setTextViewText(R.id.tvTitle, "$notificationCity, $time")

        }

        val nBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).
            setSmallIcon(R.drawable.app_icon).
            setCustomContentView(remoteView).
            setAutoCancel(true).
            setContentIntent(PendingIntent.getActivity(applicationContext,
                                                       0,
                                                       Intent(applicationContext, MainActivity::class.java),
                                                       FLAG_UPDATE_CURRENT,
                                                       null))

        return nBuilder.build()

    }

    private fun load() {

        val weather = retrofitClient.loadWeather(notificationCity)

        val timeUTC = retrofitClient.loadTimeUTC()

        if (weather != null && timeUTC != null) {

            // Weather
            val receivedTemp = weather.main.temp.toInt()
            val receivedHumidity = weather.main.humidity.toString()
            val receivedWind = weather.wind.speed.toInt()
            val receivedWindDir = weather.wind.deg
            val receivedMainDesc = weather.weather!![0].main
            val receivedDesc = weather.weather[0].description

            // Time
            val receivedTime = timeUTC.currentDateTime.substring(11..15)
            val hoursDelta = weather.timezone / 3600
            val delta = receivedTime.substring(0..1).toInt() + hoursDelta

            val time = when {
                            (delta >= 24) -> (delta - 24)
                            (delta < 0) -> (delta + 24)
                            else -> delta
                }.toString() + receivedTime.substring(2..4)

            startForeground(1, makeNotification(

                temp = setTemp(receivedTemp),
                mainDesc = getMainDescription(isNightTime(time), receivedMainDesc),
                desc = receivedDesc,
                humidity = receivedHumidity + getString(R.string.percent),
                wind = setWindSpeed(receivedWind),
                windDir = setWindDirection(receivedWindDir)!!,
                time = time

            ))
        }
    }
}
