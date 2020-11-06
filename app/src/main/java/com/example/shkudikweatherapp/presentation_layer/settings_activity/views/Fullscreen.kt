package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.*
import androidx.appcompat.app.AlertDialog
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper.Messages.restartMessage
import com.example.shkudikweatherapp.data_layer.providers.Helper.Objects.cancel
import com.example.shkudikweatherapp.data_layer.providers.Helper.Objects.restart
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
                        addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS).
                        addFlags(FLAG_ACTIVITY_CLEAR_TASK).
                        addFlags(FLAG_ACTIVITY_NEW_TASK)
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
                setPositiveButton(restart, dialogOnPositiveClickListener).
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