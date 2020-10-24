package com.example.shkudikweatherapp.presentation_layer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import kotlinx.android.synthetic.main.third_fragment_layout.*

class ThirdPart : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.third_fragment_layout, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fTemp3.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Температура"
            UserPreferences.Language.ENG -> "Temperature"
            UserPreferences.Language.GER -> "Temperatur"

        })

        fFeels3.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Ощущается как"
            UserPreferences.Language.ENG -> "Feels like"
            UserPreferences.Language.GER -> "Fühlt sich an wie"

        })

        fWind3.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Ветер"
            UserPreferences.Language.ENG -> "Wind"
            UserPreferences.Language.GER -> "Wind"

        })

        fWindDir3.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Направление ветра"
            UserPreferences.Language.ENG -> "Wind direction"
            UserPreferences.Language.GER -> "Windrichtung"

        })

        fHumidity3.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Влажность"
            UserPreferences.Language.ENG -> "Humidity"
            UserPreferences.Language.GER -> "Feuchtigkeit"

        })

        fPressure3.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Давление"
            UserPreferences.Language.ENG -> "Pressure"
            UserPreferences.Language.GER -> "Druck"

        })

        temp3.text = MoreInfo.fTemp[2]

        feels3.text = MoreInfo.fFeels[2]

        wind3.text = MoreInfo.fWind[2]

        windDir3.text = MoreInfo.fWindDir[2]

        humidity3.text = MoreInfo.fHumidity[2]

        pressure3.text = MoreInfo.fPressure[2]

    }

}