package com.example.shkudikweatherapp.presentation_layer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.adapters.MoreInfoPagerAdapter
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.more_info_fragment_layout.*

class MoreInfo : Fragment() {

    companion object {

        lateinit var time: String
        lateinit var fTemp: Array<String>
        lateinit var fFeels: Array<String>
        lateinit var fWind: Array<String>
        lateinit var fWindDir: Array<String>
        lateinit var fHumidity: Array<String>
        lateinit var fPressure: Array<String>

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.more_info_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewPager.setCurrentItem(0, true)
        viewPager.adapter = MoreInfoPagerAdapter(requireActivity())
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position ->
            viewPager.setCurrentItem(tab.position, true) }.attach()

        tabLayout.getTabAt(0)!!.text = Helper.countTime(time, 0)
        tabLayout.getTabAt(1)!!.text = Helper.countTime(time, 3)
        tabLayout.getTabAt(2)!!.text = Helper.countTime(time, 6)




    }

}