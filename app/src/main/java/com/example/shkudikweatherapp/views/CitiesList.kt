package com.example.shkudikweatherapp.views

import com.arellomobile.mvp.MvpView

interface CitiesList : MvpView {

    fun updateList(usersInput: String)
    fun hideList(usersInput: String)

}