package com.example.shkudikweatherapp.pojo.weather

import com.google.gson.annotations.SerializedName

data class Weather constructor(@SerializedName("weather")
                               val weather: List<WeatherModel>?,
                               @SerializedName("main")
                               val main: MainModel,
                               @SerializedName("wind")
                               val wind: WindModel
) {}