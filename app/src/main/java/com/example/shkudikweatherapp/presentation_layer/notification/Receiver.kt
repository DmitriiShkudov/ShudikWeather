package com.example.shkudikweatherapp.presentation_layer.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat

class Receiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        ContextCompat.startForegroundService(context, Intent(context, NotificationService::class.java))

    }

}