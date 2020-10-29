package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper.cancel
import com.example.shkudikweatherapp.data_layer.providers.Helper.reboot
import com.example.shkudikweatherapp.data_layer.providers.Helper.restartMessage
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.fullscreen
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

interface Fullscreen {

    fun setChecked(value: Boolean)
    fun setFullscreenMode(value: Boolean)

}

class FullscreenImpl(private val activity: SettingsActivity) : Fullscreen {

    override fun setChecked(value: Boolean) { with(activity) {

            val builder = AlertDialog.Builder(activity)
            val alertDialog: AlertDialog

            val dialogOnPositiveClickListener: DialogInterface.OnClickListener =
                DialogInterface.OnClickListener { dialog, which ->

                    fullscreen = value

                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    startActivity(intent)
                }

            val dialogOnNegativeClickListener =
                DialogInterface.OnClickListener { dialog, which ->

                    cbFullscreen.isChecked = fullscreen

                }

            builder.
                setMessage(restartMessage).
                setCancelable(false).
                setPositiveButton(reboot, dialogOnPositiveClickListener).
                setNegativeButton(cancel, dialogOnNegativeClickListener)

            alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun setFullscreenMode(value: Boolean) { with(activity) {

            if (value) setTheme(R.style.fullscreen)
            cbFullscreen?.isChecked = value

        }
    }
}