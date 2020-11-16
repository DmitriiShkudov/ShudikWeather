package com.example.shkudikweatherapp.presentation_layer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.data_layer.providers.Helper
import kotlinx.android.synthetic.main.second_fragment_layout.*

class SecondPart : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.second_fragment_layout, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fTemp2.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Температура"
            UserPreferences.Language.ENG -> "Temperature"
            UserPreferences.Language.GER -> "Temperatur"

        })

        fFeels2.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Ощущается как"
            UserPreferences.Language.ENG -> "Feels like"
            UserPreferences.Language.GER -> "Fühlt sich an wie"

        })

        fWind2.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Ветер"
            UserPreferences.Language.ENG -> "Wind"
            UserPreferences.Language.GER -> "Wind"

        })

        fWindDir2.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Направление ветра"
            UserPreferences.Language.ENG -> "Wind direction"
            UserPreferences.Language.GER -> "Windrichtung"

        })

        fHumidity2.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Влажность"
            UserPreferences.Language.ENG -> "Humidity"
            UserPreferences.Language.GER -> "Feuchtigkeit"

        })

        fPressure2.setText(when (UserPreferences.language) {

            UserPreferences.Language.RUS -> "Давление"
            UserPreferences.Language.ENG -> "Pressure"
            UserPreferences.Language.GER -> "Druck"

        })



        temp2.text = MoreInfo.fTemp[1]

        feels2.text = MoreInfo.fFeels[1]

        wind2.text = MoreInfo.fWind[1]

        windDir2.text = MoreInfo.fWindDir[1]

        humidity2.text = MoreInfo.fHumidity[1]

        pressure2.text = MoreInfo.fPressure[1]

    }

}