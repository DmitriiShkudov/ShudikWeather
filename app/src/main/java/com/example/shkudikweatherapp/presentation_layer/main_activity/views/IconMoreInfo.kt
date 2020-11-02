package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

interface IconMoreInfo {

    fun show(dayMode: Boolean)
    fun hide()

}

class IconMoreInfoImpl(private val activity: MainActivity): IconMoreInfo {

    override fun show(dayMode: Boolean) =
        activity.tvDescriptionIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0,
            if (dayMode) R.drawable.more_info else R.drawable.more_info_night, 0)


    override fun hide() {



    }

}