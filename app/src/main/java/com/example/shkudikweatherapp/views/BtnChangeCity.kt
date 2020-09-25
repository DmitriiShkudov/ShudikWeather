package com.example.shkudikweatherapp.views

import com.arellomobile.mvp.MvpView

interface BtnChangeCity : MvpView {

    fun applyCity()
    fun enteringCity()
    fun showResult(success: Boolean)

}