package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.Helper.LOW_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.LOW_GER
import com.example.shkudikweatherapp.data_layer.providers.Helper.LOW_RUS
import com.example.shkudikweatherapp.data_layer.providers.Helper.OVERCAST_ENG
import com.example.shkudikweatherapp.data_layer.providers.Helper.OVERCAST_GER
import com.example.shkudikweatherapp.data_layer.providers.Helper.OVERCAST_RUS
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Background
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import kotlinx.android.synthetic.main.activity_main.*

class BackgroundImpl(private val activity: MainActivity) : Background {

    override fun setBackground(description: MainDescription) { with(activity) {

            main_background.setImageDrawable(

                when (description) {

                    MainDescription.CLEAR -> {

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_clear,
                            null
                        )

                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_clear,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_clear,
                            null
                        )

                        ResourcesCompat.getDrawable(resources, R.drawable.clear, null)
                    }

                    MainDescription.CLEAR_NIGHT -> ResourcesCompat.getDrawable(resources, R.drawable.clear_night, null)



                    MainDescription.CLOUDS -> {

                        val isOvercast = desc.contains(OVERCAST_ENG) ||
                                         desc.contains(OVERCAST_RUS) ||
                                         desc.contains(OVERCAST_GER)

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_cloudy,
                            null
                        )

                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_cloudy,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_cloudy,
                            null
                        )

                        ResourcesCompat.getDrawable(
                            resources,
                            if (isOvercast) R.drawable.cloud else R.drawable.low_cloud,
                            null
                        )
                    }

                    MainDescription.CLOUDS_NIGHT -> {

                        val isOvercast = desc.contains(OVERCAST_ENG) ||
                                         desc.contains(OVERCAST_RUS) ||
                                         desc.contains(OVERCAST_GER)

                        ResourcesCompat.getDrawable(
                            resources,
                            if (isOvercast) R.drawable.cloud_night else R.drawable.low_cloud_night,
                            null
                        )

                    }

                    MainDescription.RAIN, MainDescription.DRIZZLE -> {

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_rainy,
                            null
                        )

                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_rainy,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_rainy,
                            null
                        )

                        ResourcesCompat.getDrawable(resources, R.drawable.rain, null)
                    }

                    MainDescription.RAIN_NIGHT, MainDescription.DRIZZLE_NIGHT ->
                        ResourcesCompat.getDrawable(resources, R.drawable.rain_night, null)


                    MainDescription.HAZE, MainDescription.MIST, MainDescription.DUST,
                    MainDescription.FOG, MainDescription.SMOKE, MainDescription.ASH, MainDescription.SAND -> {

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_humid,
                            null
                        )
                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_humid,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_humid,
                            null
                        )

                        ResourcesCompat.getDrawable(resources, R.drawable.humid, null)
                    }

                    MainDescription.HAZE_NIGHT, MainDescription.MIST_NIGHT, MainDescription.DUST_NIGHT,
                    MainDescription.FOG_NIGHT, MainDescription.SMOKE_NIGHT, MainDescription.ASH_NIGHT,
                    MainDescription.SAND_NIGHT-> {

                        ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)

                    }

                    MainDescription.SNOW -> {

                        val isLow = desc.contains(LOW_ENG) ||
                                    desc.contains(LOW_RUS) ||
                                    desc.contains(LOW_GER)

                        tempIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_snow,
                            null
                        )
                        windIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_snow,
                            null
                        )
                        humidityIcon.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.back_icons_snow,
                            null
                        )

                        ResourcesCompat.getDrawable(
                            resources, if (isLow) R.drawable.low_snow else
                                R.drawable.snow, null
                        )
                    }

                    MainDescription.SNOW_NIGHT ->
                        ResourcesCompat.getDrawable(resources, R.drawable.snow_night, null)

                    MainDescription.THUNDERSTORM, MainDescription.THUNDERSTORM_NIGHT ->
                        ResourcesCompat.getDrawable(resources, R.drawable.thunder, null)

                    MainDescription.TORNADO, MainDescription.TORNADO_NIGHT ->
                        ResourcesCompat.getDrawable(resources, R.drawable.tornado, null)

                    else -> ResourcesCompat.getDrawable(resources, R.drawable.humid_night, null)

                })
        }
    }

}