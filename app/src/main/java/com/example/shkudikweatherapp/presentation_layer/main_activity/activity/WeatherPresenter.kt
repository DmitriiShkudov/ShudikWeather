package com.example.shkudikweatherapp.presentation_layer.main_activity.activity

import com.example.shkudikweatherapp.R
import com.example.shkudikweatherapp.data_layer.pojo.forecast.Forecast
import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import com.example.shkudikweatherapp.data_layer.pojo.weather.Weather
import com.example.shkudikweatherapp.data_layer.providers.Helper
import com.example.shkudikweatherapp.data_layer.providers.Helper.fahrenheit
import com.example.shkudikweatherapp.data_layer.providers.Helper.value
import com.example.shkudikweatherapp.data_layer.providers.UserPreferences
import com.example.shkudikweatherapp.presentation_layer.main_activity.states.MainStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherPresenter {

    suspend fun loadedPresentation(weather: Weather, timeUTC: TimeUTC, forecast: Forecast, viewModel: MainViewModel) {

        with(viewModel) {

            // Received weather
            val receivedTemp = weather.main.temp.toInt() - Helper.ABS_ZERO
            val receivedHumidity =
                weather.main.humidity.toString() + app.getString(R.string.percent)
            val receivedWind = weather.wind.speed.toInt()
            val receivedMainDesc = weather.weather!![0].main
            val receivedDesc = weather.weather[0].description

            // Received time
            val receivedTime = timeUTC.currentDateTime.substring(11..15)

            val hoursDelta = weather.timezone / 3600
            val delta = receivedTime.substring(0..1).toInt() + hoursDelta

            // Received forecast
            val receivedFTemp1 = weather.main.temp.toInt()
            val receivedFTemp2 = forecast.list[1].main.temp.toInt()
            val receivedFTemp3 = forecast.list[2].main.temp.toInt()

            val receivedFFeels1 = weather.main.feels_like.toInt()
            val receivedFFeels2 = forecast.list[1].main.feels_like.toInt()
            val receivedFFeels3 = forecast.list[2].main.feels_like.toInt()

            val receivedFWind1 = weather.wind.speed.toInt()
            val receivedFWind2 = forecast.list[1].windModel.speed.toInt()
            val receivedFWind3 = forecast.list[2].windModel.speed.toInt()

            val receivedFWindDir1 = weather.wind.deg
            val receivedFWindDir2 = forecast.list[1].windModel.deg
            val receivedFWindDir3 = forecast.list[2].windModel.deg

            val receivedFPressure1 = weather.main.pressure
            val receivedFPressure2 = forecast.list[1].main.pressure
            val receivedFPressure3 = forecast.list[2].main.pressure

            // applying received info

            //
            withContext(Dispatchers.Default) {
                time.postValue(
                    (when {
                        (delta >= 24) -> (delta - 24)
                        (delta < 0) -> (delta + 24)
                        else -> delta
                    }).toString() + receivedTime.substring(2..4)
                )
            }
            //
            withContext(Dispatchers.Default) {
                desc.postValue("$receivedDesc, ${time.value!!}")
                isNight.postValue(Helper.isNightTime(time.value!!))
            }
            //
            withContext(Dispatchers.Default) {
                mainDesc.postValue(Helper.getMainDescription(isNight.value!!, receivedMainDesc))
            }

            state.value(MainStates.UPDATED)

            withContext(Dispatchers.Main) {
                // Setting forecast

                fTemp.value(
                    arrayOf(
                        Helper.setTemp(receivedFTemp1),
                        Helper.setTemp(receivedFTemp2),
                        Helper.setTemp(receivedFTemp3)
                    )
                )

                fFeels.value(
                    arrayOf(
                        Helper.setTemp(receivedFFeels1),
                        Helper.setTemp(receivedFFeels2),
                        Helper.setTemp(receivedFFeels3)
                    )
                )

                fWind.value(
                    arrayOf(
                        Helper.setWindSpeed(receivedFWind1),
                        Helper.setWindSpeed(receivedFWind2),
                        Helper.setWindSpeed(receivedFWind3)
                    )
                )

                fWindDir.value(
                    arrayOf(
                        Helper.setWindDirection(receivedFWindDir1)!!,
                        Helper.setWindDirection(receivedFWindDir2)!!,
                        Helper.setWindDirection(receivedFWindDir3)!!
                    )
                )

                fHumidity.value(
                    arrayOf(
                        weather.main.humidity.toString() + app.getString(R.string.percent),
                        forecast.list[1].main.humidity.toString() + app.getString(R.string.percent),
                        forecast.list[2].main.humidity.toString() + app.getString(R.string.percent)
                    )
                )

                fPressure.value(
                    arrayOf(
                        Helper.setPressure(receivedFPressure1),
                        Helper.setPressure(receivedFPressure2),
                        Helper.setPressure(receivedFPressure3)
                    )
                )

                // Setting current weather

                temp.value(
                    (if (UserPreferences.degreeUnit == UserPreferences.TemperatureUnit.DEG_C)
                        (receivedTemp) else (receivedTemp).fahrenheit()).toString() + UserPreferences.degreeUnit.str
                )

                humidity.value(receivedHumidity)

                wind.value(receivedWind.toString() + Helper.Units.windUnit)

            }
        }

    }

}