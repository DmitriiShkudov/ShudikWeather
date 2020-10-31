package com.example.shkudikweatherapp.presentation_layer.common_protocols

import com.example.shkudikweatherapp.data_layer.enums.MainDescription

interface Background {

    fun setBackground(description: MainDescription)

}