package com.example.shkudikweatherapp.presentation_layer.common_protocols

import com.example.shkudikweatherapp.data_layer.states.MainDescription

interface Background {

    fun setBackground(description: MainDescription)

}