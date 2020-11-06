package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import androidx.core.content.ContextCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.enums.MainDescription
import com.example.shkudikweatherapp.data_layer.enums.MainDescription.*
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.desc
import com.example.shkudikweatherapp.presentation_layer.common_protocols.Background
import kotlinx.android.synthetic.main.activity_main.*

class BackgroundImpl(private val activity: MainActivity) : Background {

    override fun setBackground(description: MainDescription) { with(activity) {

            main_background.setImageDrawable(

                when (description) {

                    CLEAR -> {

                        tempIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_clear)

                        windIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_clear)

                        humidityIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_clear)

                        ContextCompat.getDrawable(activity, R.drawable.clear)

                    }

                    CLEAR_NIGHT -> ContextCompat.getDrawable(activity, R.drawable.clear_night)

                    CLOUDS -> {

                        val isOvercast = desc.contains(getString(R.string.overcast_rus)) ||
                                         desc.contains(getString(R.string.overcast_eng)) ||
                                         desc.contains(getString(R.string.overcast_ger))

                        tempIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_cloudy)

                        windIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_cloudy)

                        humidityIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_cloudy)

                        ContextCompat.getDrawable(activity, if (isOvercast) R.drawable.cloud else R.drawable.low_cloud)

                    }

                    CLOUDS_NIGHT -> {

                        val isOvercast = desc.contains(getString(R.string.overcast_rus)) ||
                                         desc.contains(getString(R.string.overcast_eng)) ||
                                         desc.contains(getString(R.string.overcast_ger))

                        ContextCompat.getDrawable(activity,
                            if (isOvercast) R.drawable.cloud_night else R.drawable.low_cloud_night)

                    }

                    RAIN, DRIZZLE -> {

                        tempIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_rainy)

                        windIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_rainy)

                        humidityIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_rainy)

                        ContextCompat.getDrawable(activity, R.drawable.rain)

                    }

                    RAIN_NIGHT, DRIZZLE_NIGHT -> ContextCompat.getDrawable(activity, R.drawable.rain_night)


                    HAZE, MIST, DUST, FOG, SMOKE, ASH, SAND -> {

                        tempIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_humid)

                        windIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_humid)

                        humidityIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_humid)

                        ContextCompat.getDrawable(activity, R.drawable.humid)

                    }

                    HAZE_NIGHT, MIST_NIGHT, DUST_NIGHT, FOG_NIGHT, SMOKE_NIGHT, ASH_NIGHT, SAND_NIGHT ->
                        ContextCompat.getDrawable(activity, R.drawable.humid_night)

                    SNOW -> {

                        val isLow = desc.contains(getString(R.string.low_rus)) ||
                                    desc.contains(getString(R.string.low_eng)) ||
                                    desc.contains(getString(R.string.low_ger))

                        tempIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_snow)

                        windIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_snow)

                        humidityIcon.background = ContextCompat.getDrawable(activity, R.drawable.back_icons_snow)

                        ContextCompat.getDrawable(activity, if (isLow) R.drawable.low_snow else R.drawable.snow)
                    }

                    SNOW_NIGHT -> ContextCompat.getDrawable(activity, R.drawable.snow_night)

                    THUNDERSTORM, THUNDERSTORM_NIGHT -> ContextCompat.getDrawable(activity, R.drawable.thunder)

                    TORNADO, TORNADO_NIGHT -> ContextCompat.getDrawable(activity, R.drawable.tornado)

                    else -> ContextCompat.getDrawable(activity, R.drawable.humid_night)

                })
        }
    }
}