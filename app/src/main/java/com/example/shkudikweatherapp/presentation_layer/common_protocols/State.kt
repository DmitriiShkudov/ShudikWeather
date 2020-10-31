package com.example.shkudikweatherapp.presentation_layer.common_protocols

interface State {

    fun <T> setState(state: T)

}