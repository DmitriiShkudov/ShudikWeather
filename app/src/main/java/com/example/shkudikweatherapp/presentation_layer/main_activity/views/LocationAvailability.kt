package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.Manifest
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.presentation_layer.common_protocols.IError
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity.Companion.REQUEST_PERMISSION_CODE

interface LocationAvailability: IError {

    fun askPermission()

}

class LocationAvailabilityImpl(private val activity: MainActivity) : LocationAvailability {

    override fun askPermission() =
        ActivityCompat.requestPermissions(activity,
            Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION_CODE)

    override fun showError(message: String) =
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

}