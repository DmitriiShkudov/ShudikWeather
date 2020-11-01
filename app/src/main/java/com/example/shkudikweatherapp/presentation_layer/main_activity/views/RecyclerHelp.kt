package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.opengl.Visibility
import android.view.View
import android.view.View.GONE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shkudikweatherapp.presentation_layer.adapters.RvHelpAdapter
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.helpList
import kotlinx.android.synthetic.main.activity_main.*

interface RecyclerHelp {

    fun update()
    fun hide()

}

class RecyclerHelpImpl(private val activity: MainActivity) : RecyclerHelp {

    override fun update() { with(activity) {

            rvHelp.adapter = RvHelpAdapter(this@RecyclerHelpImpl, activity.boardImpl, helpList)
            rvHelp.layoutManager = LinearLayoutManager(applicationContext)

        }
    }

    override fun hide() = activity.rvHelp.setVisibility(GONE)

}