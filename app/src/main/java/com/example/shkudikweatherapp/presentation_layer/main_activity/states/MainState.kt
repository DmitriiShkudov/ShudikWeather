package com.example.shkudikweatherapp.presentation_layer.main_activity.states

import android.graphics.Typeface.NORMAL
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY
import com.example.shkudikweatherapp.data_layer.providers.Helper.Objects.location
import com.example.shkudikweatherapp.data_layer.providers.Helper.hideKeyboard
import com.example.shkudikweatherapp.data_layer.providers.Helper.setSafeOnClickListener
import com.example.shkudikweatherapp.data_layer.providers.Helper.showKeyboard
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedCity
import com.example.shkudikweatherapp.presentation_layer.common_protocols.State
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.MORE_INFO
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.CHANGING_CITY
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.CITY_APPLIED
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.CHANGING_CITY_CANCELLED
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.BAD_CONNECTION
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.LOADING
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates.UPDATED
import kotlinx.android.synthetic.main.activity_main.*

class MainStateImpl(private val activity: MainActivity) : State {

    override fun <MainStates> setState(state: MainStates) { with(activity) {

            when (state) {

                MORE_INFO -> {

                    moreInfoImpl.attach()
                    root.setSafeOnClickListener { moreInfoImpl.detach() }

                }

                CHANGING_CITY -> {

                    tvDescriptionIcon.visibility = View.INVISIBLE
                    rvHelp.visibility = View.VISIBLE
                    btn_apply_city.isClickable = true

                    with(input_city) {

                        setTypeface(null, NORMAL)
                        input_city_frame.descendantFocusability = FOCUS_BEFORE_DESCENDANTS
                        setText(EMPTY)
                        requestFocus()
                        showKeyboard(applicationContext)

                    }

                    root.setSafeOnClickListener {
                        setState(CHANGING_CITY_CANCELLED)
                    }

                    recyclerHelpImpl.update()

                }

                CHANGING_CITY_CANCELLED -> {

                    // cancellation
                    btn_apply_city.isClickable = false
                    btn_change_city.isClickable = true
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    tvDescriptionIcon.visibility = View.VISIBLE
                    recyclerHelpImpl.hide()

                    with(input_city) {
                        clearFocus()
                        hideKeyboard(applicationContext)

                        setText(if (searchMode == UserPreferences.SearchMode.CITY) selectedCity
                            else location)
                    }

                }

                LOADING -> {

                    with(input_city) {
                        background = ResourcesCompat.getDrawable(resources, R.drawable.input_empty, null)
                        clearFocus()
                        hideKeyboard(applicationContext)
                    }

                    btn_change_city.isClickable = false
                    btn_geo.isClickable = false
                    btn_apply_city.isClickable = false
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    cpv_loading.visibility = View.VISIBLE
                    recyclerHelpImpl.hide()

                }

                CITY_APPLIED -> tvDescriptionIcon.visibility = View.VISIBLE

                UPDATED -> {

                    btn_change_city.isClickable = true
                    btn_apply_city.isClickable = false
                    btn_geo.isClickable = true
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.GONE
                    recyclerHelpImpl.update()
                    changeableSearchModeImpl.setEnabledSearchMode()
                    localeImpl.setLocale()

                    if (searchMode == UserPreferences.SearchMode.CITY) {
                        boardImpl.setCity()
                    } else {
                        boardImpl.setUserLocationTitle()
                    }

                }

                BAD_CONNECTION -> {

                    cpv_loading.visibility = View.GONE
                    bad_connection.visibility = View.VISIBLE

                }

                else -> {

                    setState(UPDATED)

                    cpv_loading.visibility = View.GONE

                    tvDescriptionIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                    windIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)

                    humidityIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)

                    tempIcon.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.back_icons_wrong, null)

                    input_city.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.back_city_input_wrong,
                        null
                    )

                    getString(R.string.minus).also {

                        tvWind.text = it
                        tvHumidity.text = it
                        tvTemp.text = it

                    }
                }
            }
        }
    }
}