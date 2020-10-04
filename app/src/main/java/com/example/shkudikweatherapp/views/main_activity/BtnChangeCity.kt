package com.example.shkudikweatherapp.views.main_activity

import com.arellomobile.mvp.MvpView

interface BtnChangeCity : MvpView {

    fun applyCity()
    fun enteringCity()
    fun cancel()

}