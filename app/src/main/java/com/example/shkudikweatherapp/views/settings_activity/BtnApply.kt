package com.example.shkudikweatherapp.views.settings_activity

import com.arellomobile.mvp.MvpView

interface BtnApply : MvpView {


    fun success()
    fun fail(msg: String)

}