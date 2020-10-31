package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.graphics.Typeface.NORMAL
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.EMPTY
import com.example.shkudikweatherapp.data_layer.providers.Helper.hideKeyboard
import com.example.shkudikweatherapp.data_layer.providers.Helper.locationTitle
import com.example.shkudikweatherapp.data_layer.providers.Helper.setSafeOnClickListener
import com.example.shkudikweatherapp.data_layer.providers.Helper.showKeyboard
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences.searchMode
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider.selectedCity
import com.example.shkudikweatherapp.data_layer.states.States
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

interface State {

    fun setState(state: States)

}

class StateImpl(private val activity: MainActivity) : State {

    override fun setState(state: States) { with(activity) {

            when (state) {

                States.MORE_INFO -> {

                    moreInfoImpl.attach()
                    root.setSafeOnClickListener { moreInfoImpl.detach() }

                }

                States.CHANGING_CITY -> {

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

                    root.setSafeOnClickListener { setState(States.CHANGING_CITY_CANCELLED) }

                    recyclerHelpImpl.update()

                }

                States.CHANGING_CITY_CANCELLED -> {

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
                            else locationTitle)
                    }

                }

                States.LOADING -> {

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

                States.CITY_APPLIED -> tvDescriptionIcon.visibility = View.VISIBLE

                States.UPDATED -> {

                    btn_change_city.isClickable = true
                    btn_geo.isClickable = true
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.GONE
                    changeableSearchModeImpl.setEnabledSearchMode()

                }

                States.BAD_CONNECTION -> {
                    cpv_loading.visibility = View.GONE
                    bad_connection.visibility = View.VISIBLE
                }

                States.WRONG_CITY -> {

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

                    Helper.MINUS.also {

                        tvWind.text = it
                        tvHumidity.text = it
                        tvTemp.text = it

                    }

                    changeableSearchModeImpl.setEnabledSearchMode()

                }

            }
        }

    }

}