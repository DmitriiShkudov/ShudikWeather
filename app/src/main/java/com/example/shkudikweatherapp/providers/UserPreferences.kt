package com.example.shkudikweatherapp.providers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings.Secure.getString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.activities.MainActivity
import com.example.shkudikweatherapp.data.InfoDatabase

object UserPreferences {

    enum class Language(val str: String) { RUS("rus"), ENG("eng"), GER("ger") }

    private const val USER_PREF = "User pref"
    private const val NOTIF_CITY_KEY = "City key"
    private const val NOTIF_INTERVAL_KEY = "Interval key"
    private const val LANG_KEY = "Language key"
    private const val FULLSCREEN_KEY = "Fullscreen key"

    var context: Context? = null

    val db: InfoDatabase
    get() = Room.databaseBuilder(this.context!!, InfoDatabase::class.java, "info").build()

    val pref: SharedPreferences?
    get() = this.context?.getSharedPreferences(USER_PREF, 0)


    var notifCity: String
    get() = this.pref?.getString(NOTIF_CITY_KEY, "") ?: ""
    set(value) = this.pref?.edit()?.putString(NOTIF_CITY_KEY, value)?.apply()!!


    var notifInterval: Int
    get() = this.pref?.getInt(NOTIF_INTERVAL_KEY, 2) ?: 2
    set(value) = this.pref?.edit()?.putInt(NOTIF_INTERVAL_KEY, value)?.apply()!!


    var language: Language
    get() = when (this.pref?.getString(LANG_KEY, Language.ENG.str)) {

        "rus" -> Language.RUS
        "eng" -> Language.ENG
        else -> Language.GER
    }
    set(value) {

        this.pref?.edit()?.putString(LANG_KEY, value.str)?.apply()

    }

    var fullscreen: Boolean
    get() = this.pref?.getBoolean(FULLSCREEN_KEY, false) ?: false
    set(value) = this.pref?.edit()?.putBoolean(FULLSCREEN_KEY, value)?.apply()!!

    fun makeNotification() {

        val aContext = this.context!!.applicationContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "F"
            val descriptionText = "F"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("D", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                aContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // Create an explicit intent for an Activity in your app
            val intent = Intent(aContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(aContext, 0, intent, 0)

            val builder = NotificationCompat.Builder(aContext, "G")
                .setSmallIcon(R.drawable.england)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val id = 12

            with(NotificationManagerCompat.from(aContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(id, builder.build())
            }

        }




    }

}