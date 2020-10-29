package com.example.shkudikweatherapp.presentation_layer.common_protocols

interface TimeMode {

    fun setDayMode()
    fun setNightMode()
    fun setTimeMode(isNight: Boolean)

}