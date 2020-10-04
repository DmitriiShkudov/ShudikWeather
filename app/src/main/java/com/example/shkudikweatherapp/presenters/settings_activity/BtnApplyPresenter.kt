package com.example.shkudikweatherapp.presenters.settings_activity

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.shkudikweatherapp.providers.UserPreferences
import com.example.shkudikweatherapp.views.settings_activity.BtnApply

@InjectViewState
class BtnApplyPresenter : MvpPresenter<BtnApply>() {

    var selectedHours = 0
    var selectedCity = String()

    val handler = Handler {

        when (it.what) {

            -1 -> viewState.fail("This city isn't exist")

            else -> {

                UserPreferences.notifCity = this.selectedCity
                UserPreferences.notifInterval = this.selectedHours
                UserPreferences.makeNotification()


                viewState.success()

            }

        }

        true

    }

    fun check(notificationCity: String, hours: Int) {

        if (notificationCity.isEmpty()) {
            viewState.fail("Fill the fields")
            return
        }

        this.selectedHours = hours
        this.selectedCity = notificationCity

    }

}