package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.view.View
import android.view.View.GONE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shkudikweatherapp.presentation_layer.adapters.RvHelpAdapter
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.helpList
import kotlinx.android.synthetic.main.activity_main.*

interface RecyclerHelp {

    fun update()
    fun hide()
    fun show()

}

class RecyclerHelpImpl(private val activity: MainActivity) : RecyclerHelp {

    override fun update() { with(activity) {

            rvHelp.adapter = RvHelpAdapter(this@RecyclerHelpImpl, activity.boardImpl, helpList)
            rvHelp.layoutManager = LinearLayoutManager(applicationContext)

        }
    }

    override fun hide() { with(activity) {

            rvHelp.animate().apply {

                duration = 1000
                scaleX(0.66f)
                scaleY(0.66f)
                alpha(0f)

            }

        }
    }


    override fun show() { with(activity) {

            rvHelp.visibility = View.VISIBLE
            rvHelp.animate().apply {

                duration = 1000
                scaleX(1.0f)
                scaleY(1.0f)
                alpha(1f)

            }
        }

    }

}