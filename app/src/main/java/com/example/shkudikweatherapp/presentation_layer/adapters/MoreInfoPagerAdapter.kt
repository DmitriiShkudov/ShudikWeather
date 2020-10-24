package com.example.shkudikweatherapp.presentation_layer.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shkudikweatherapp.presentation_layer.fragments.FirstPart
import com.example.shkudikweatherapp.presentation_layer.fragments.SecondPart
import com.example.shkudikweatherapp.presentation_layer.fragments.ThirdPart

class MoreInfoPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int) = when (position) {

        0 -> FirstPart()
        1 -> SecondPart()
        else -> ThirdPart()

    }

}