package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.Manifest
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences

interface LocationAvailability {

    fun askPermission()
    fun showDenyError()

}

class LocationAvailabilityImpl(private val activity: MainActivity) : LocationAvailability {

    override fun askPermission() =
        ActivityCompat.requestPermissions(activity,
            Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, 44)



    override fun showDenyError() {

        Toast.makeText(activity, when (UserPreferences.language) {

            UserPreferences.Language.RUS -> Helper.LOCATION_DENIED_WARNING_RUS
            UserPreferences.Language.ENG -> Helper.LOCATION_DENIED_WARNING_ENG
            UserPreferences.Language.GER -> Helper.LOCATION_DENIED_WARNING_GER

        }, Toast.LENGTH_LONG).show()

    }

}