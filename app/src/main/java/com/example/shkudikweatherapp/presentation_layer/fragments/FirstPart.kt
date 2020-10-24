package com.example.shkudikweatherapp.presentation_layer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import kotlinx.android.synthetic.main.first_fragment_layout.*

class FirstPart : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.first_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fTemp1.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Температура"
            UserPreferences.Language.ENG -> "Temperature"
            UserPreferences.Language.GER -> "Temperatur"

        })

        fFeels1.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Ощущается как"
            UserPreferences.Language.ENG -> "Feels like"
            UserPreferences.Language.GER -> "Fühlt sich an wie"

        })

        fWind1.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Ветер"
            UserPreferences.Language.ENG -> "Wind"
            UserPreferences.Language.GER -> "Wind"

        })

        fWindDir1.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Направление ветра"
            UserPreferences.Language.ENG -> "Wind direction"
            UserPreferences.Language.GER -> "Windrichtung"

        })

        fHumidity1.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Влажность"
            UserPreferences.Language.ENG -> "Humidity"
            UserPreferences.Language.GER -> "Feuchtigkeit"

        })

        fPressure1.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Давление"
            UserPreferences.Language.ENG -> "Pressure"
            UserPreferences.Language.GER -> "Druck"

        })



        temp1.text = MoreInfo.fTemp[0]

        feels1.text = MoreInfo.fFeels[0]

        wind1.text = MoreInfo.fWind[0]

        windDir1.text = MoreInfo.fWindDir[0]

        humidity1.text = MoreInfo.fHumidity[0]

        pressure1.text = MoreInfo.fPressure[0]

    }

}