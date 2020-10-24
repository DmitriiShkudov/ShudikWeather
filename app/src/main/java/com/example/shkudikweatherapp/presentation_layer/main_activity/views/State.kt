package com.example.shkudikweatherapp.presentation_layer.main_activity.views

import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.MainActivity
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.hideKeyboard
import com.example.shkudikweatherapp.presentation_layer.main_activity.activity.showKeyboard
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.WeatherProvider
import com.example.shkudikweatherapp.presentation_layer.states.States
import kotlinx.android.synthetic.main.activity_main.*

interface State {

    fun setState(state: States)

}

class StateImpl(private val activity: MainActivity,
                private val moreInfoImpl: MoreInfoImpl,
                private val recyclerHelpImpl: RecyclerHelpImpl,
                private val changebleSearchModeImpl: ChangebleSearchModeImpl,
                private val timeModeImpl: TimeModeImpl) : State {

    override fun setState(state: States) { with(activity) {

            when (state) {

                States.MORE_INFO -> {

                    moreInfoImpl.attach()
                    root.setOnClickListener {

                        moreInfoImpl.detach()
                        root.setOnClickListener(null)

                    }

                }

                States.CHANGING_CITY -> {

                    tvDescriptionIcon.visibility = View.INVISIBLE
                    rvHelp.visibility = View.VISIBLE
                    btn_apply_city.isClickable = true

                    with(input_city) {

                        setTypeface(null, android.graphics.Typeface.NORMAL)
                        input_city_frame.descendantFocusability =
                            ViewGroup.FOCUS_BEFORE_DESCENDANTS
                        setText(Helper.EMPTY)
                        requestFocus()
                        showKeyboard(applicationContext)

                    }

                    root.setOnClickListener {

                        setState(States.CHANGING_CITY_CANCELLED)

                    }

                    recyclerHelpImpl.update()

                }

                States.CHANGING_CITY_CANCELLED -> {

                    // cancellation
                    btn_apply_city.isClickable = false
                    btn_change_city.isClickable = true
                    input_city.clearFocus()
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    input_city.hideKeyboard(applicationContext)

                    input_city.setText(
                        if
                                (UserPreferences.searchMode == UserPreferences.SearchMode.CITY) WeatherProvider.selectedCity
                        else when (UserPreferences.language) {
                            UserPreferences.Language.RUS -> Helper.YOUR_LOCATION_RUS
                            UserPreferences.Language.ENG -> Helper.YOUR_LOCATION_ENG
                            UserPreferences.Language.GER -> Helper.YOUR_LOCATION_GER
                        }
                    )

                    main_background.setOnClickListener(null)
                    tvDescriptionIcon.visibility = View.VISIBLE

                    recyclerHelpImpl.hide()

                }

                States.LOADING -> {

                    input_city.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.input_empty, null)

                    btn_change_city.isClickable = true
                    btn_apply_city.isClickable = false
                    input_city.clearFocus()
                    input_city_frame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    input_city.hideKeyboard(applicationContext)
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.VISIBLE
                    recyclerHelpImpl.hide()
                }

                States.CITY_APPLIED -> tvDescriptionIcon.visibility = View.VISIBLE

                States.UPDATED -> {

                    btn_change_city.isClickable = true
                    bad_connection.visibility = View.GONE
                    cpv_loading.visibility = View.GONE

                    changebleSearchModeImpl.setEnabledSearchMode()

                }

                States.BAD_CONNECTION -> {

                    bad_connection.visibility = View.VISIBLE
                    cpv_loading.visibility = View.GONE

                }

                States.WRONG_CITY -> {

                    cpv_loading.visibility = View.GONE
                    bad_connection.visibility = View.GONE
                    tvDescriptionIcon.visibility = View.VISIBLE
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

                    timeModeImpl.setDayMode()
                    changebleSearchModeImpl.setEnabledSearchMode()

                }

            }
        }

    }

}