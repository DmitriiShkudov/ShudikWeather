package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.isSelectedCityExists
import com.example.shkudikweatherapp.presentation_layer.common_protocols.TimeMode
import kotlinx.android.synthetic.main.activity_main.*

class TimeModeImpl(val activity: MainActivity) : TimeMode {

    override fun setDayMode() { with(activity) {

            if (isSelectedCityExists)
                tvDescriptionIcon.
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.more_info, 0)

            btn_apply_city.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.apply_city,
                    null
                )
            )

            btn_geo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.location, null))

            btn_change_city.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.replace,
                    null
                )
            )

            btn_settings.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.settings,
                    null
                )
            )

            ResourcesCompat.getColor(resources, R.color.black, null).also {

                tvDescriptionIcon.setTextColor(it)
                input_city.setTextColor(it)
                tvTemp.setTextColor(it)
                tvHumidity.setTextColor(it)
                tvWind.setTextColor(it)
                text_humidity.setTextColor(it)
                text_wind.setTextColor(it)
                text_temp.setTextColor(it)

            }

            input_city.background =
                ResourcesCompat.getDrawable(resources, R.drawable.back_city_input, null)
        }

    }

    override fun setNightMode() { with(activity) {

            if (isSelectedCityExists)
                tvDescriptionIcon.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.more_info_night,
                    0
                )


            btn_change_city.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.replace_night,
                    null
                )
            )
            btn_geo.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.location_night,
                    null
                )
            )
            btn_settings.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.settings_night,
                    null
                )
            )
            btn_apply_city.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.apply_city_night,
                    null
                )
            )

            ResourcesCompat.getColor(resources, R.color.white, null).also {

                input_city.setTextColor(it)
                tvTemp.setTextColor(it)
                tvHumidity.setTextColor(it)
                tvWind.setTextColor(it)
                text_humidity.setTextColor(it)
                text_wind.setTextColor(it)
                text_temp.setTextColor(it)

            }

            tvDescriptionIcon.setTextColor(ResourcesCompat.getColor(resources,
                                                                    R.color.bright_white,
                                                                    null))

            tempIcon.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.back_icons_night,
                null
            )

            windIcon.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.back_icons_night,
                null
            )
            humidityIcon.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.back_icons_night,
                null
            )

            input_city.background =
                ResourcesCompat.getDrawable(resources, R.drawable.back_city_input_night, null)
        }

    }

    override fun setTimeMode(isNight: Boolean) = if (isNight) setNightMode() else setDayMode()


}