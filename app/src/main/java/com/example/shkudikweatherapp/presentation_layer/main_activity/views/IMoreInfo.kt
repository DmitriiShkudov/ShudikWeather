package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import androidx.fragment.app.FragmentTransaction
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.fragments.MoreInfo
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity

interface IMoreInfo {

    fun attach()
    fun detach()

}

class MoreInfoImpl(private val activity: MainActivity) : IMoreInfo {

    override fun attach() { with(activity) {

        supportFragmentManager.
            beginTransaction().
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
            addToBackStack(null).
            replace(R.id.main_layout, MoreInfo(), MainActivity.FRAGMENT_TAG).
            commit()

        }

    }

    override fun detach() { with(activity) {

        supportFragmentManager.
            beginTransaction().
            detach(supportFragmentManager.findFragmentByTag(MainActivity.FRAGMENT_TAG)!!).
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).
            commit()

        }
    }

}