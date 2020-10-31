package com.example.shkudikweatherapp.presentation_layer.settings_activity.views

import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.OVERCAST_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.OVERCAST_GER
import com.example.shkudikweatherapp.data_layer.providers.Helper.OVERCAST_RUS
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.mainDesc
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Background
import com.example.shkudikweatherapp.presentation_layer.settings_activity.activity.SettingsActivity
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import kotlinx.android.synthetic.main.activity_settings.*

class BackgroundImpl(private val activity: SettingsActivity, private val timeModeImpl: TimeModeImpl) : Background {

    override fun setBackground(description: MainDescription) { with(activity) {

        settingsBackground.setImageDrawable(

            when (mainDesc) {

                MainDescription.CLEAR -> {

                    timeModeImpl.setDayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.clear, null)

                }

                MainDescription.CLEAR_NIGHT -> {

                    timeModeImpl.setNightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.clear_night, null)

                }

                MainDescription.CLOUDS -> {

                    val isOvercast = desc.contains(OVERCAST_ENG) ||
                                     desc.contains(OVERCAST_RUS) ||
                                     desc.contains(OVERCAST_GER)

                    timeModeImpl.setDayMode()
                    ResourcesCompat.getDrawable(resources,
                        if (isOvercast) R.drawable.cloud else R.drawable.low_cloud, null)
                }

                MainDescription.CLOUDS_NIGHT -> {

                    val isOvercast = desc.contains(OVERCAST_ENG) ||
                                     desc.contains(OVERCAST_RUS) ||
                                     desc.contains(OVERCAST_GER)

                    timeModeImpl.setNightMode()
                    ResourcesCompat.getDrawable(resources,
                        if (isOvercast) R.drawable.cloud_night else R.drawable.low_cloud_night, null)

                }

                MainDescription.RAIN -> {

                    timeModeImpl.setDayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.rain, null)

                }

                MainDescription.RAIN_NIGHT -> {

                    timeModeImpl.setNightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.rain_night, null)

                }

                MainDescription.HAZE, MainDescription.MIST, MainDescription.DUST,
                MainDescription.FOG, MainDescription.SMOKE -> {

                    timeModeImpl.setDayMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.humid, null)

                }

                MainDescription.HAZE_NIGHT, MainDescription.MIST_NIGHT, MainDescription.DUST_NIGHT,
                MainDescription.FOG_NIGHT, MainDescription.SMOKE_NIGHT -> {

                    timeModeImpl.setNightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)

                }

                MainDescription.SNOW -> {

                    val isLow = desc.contains(Helper.LOW_ENG) ||
                                desc.contains(Helper.LOW_RUS) ||
                                desc.contains(Helper.LOW_GER)

                    timeModeImpl.setDayMode()
                    ResourcesCompat.getDrawable(resources,
                        if (isLow) R.drawable.low_snow else R.drawable.snow, null)

                }

                MainDescription.SNOW_NIGHT -> {

                    timeModeImpl.setNightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.snow_night, null)

                }

                else -> {
                    timeModeImpl.setNightMode()
                    ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)
                }

            })
        }
    }

}