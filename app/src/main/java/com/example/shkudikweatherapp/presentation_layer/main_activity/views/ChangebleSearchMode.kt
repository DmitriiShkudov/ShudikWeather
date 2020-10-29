package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import kotlinx.android.synthetic.main.activity_main.*

interface ChangeableSearchMode {

    fun setEnabledSearchMode()

}

class ChangeableSearchModeImpl(private val activity: MainActivity) : ChangeableSearchMode {

    override fun setEnabledSearchMode() { with(activity) {

            when (searchMode) {

                UserPreferences.SearchMode.CITY -> {

                    btn_change_city.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            if (WeatherProvider.isNight) R.drawable.replace_enabled_night else R.drawable.replace_enabled,
                            null
                        )
                    )

                    btn_geo.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            if (WeatherProvider.isNight) R.drawable.location_night else R.drawable.location,
                            null
                        )
                    )

                }

                UserPreferences.SearchMode.GEO -> {

                    btn_change_city.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            if (WeatherProvider.isNight) R.drawable.replace_night else R.drawable.replace,
                            null
                        )
                    )

                    btn_geo.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            if (WeatherProvider.isNight) R.drawable.location_enabled_night else R.drawable.location_enabled,
                            null
                        )
                    )
                }
            }
        }
    }
}