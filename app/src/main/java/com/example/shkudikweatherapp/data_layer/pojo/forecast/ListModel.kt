package com.example.shkudikweatherapp.data_layer.pojo.forecast

import com.example.shkudikweatherapp.data_layer.pojo.weather.WeatherModel
import com.example.shkudikweatherapp.data_layer.pojo.weather.WindModel
import com.google.gson.annotations.SerializedName

data class ListModel constructor(@SerializedName("main")
                                 val main: ForecastMainModel,
                                 @SerializedName("weather")
                                 val weather: List<WeatherModel>,
                                 @SerializedName("wind")
                                 val windModel: WindModel) {
}