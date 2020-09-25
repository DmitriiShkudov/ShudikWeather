package com.example.shkudikweatherapp.pojo.weather

data class Weather constructor(val weather: List<WeatherModel>?,
                               val main: MainModel,
                               val wind: WindModel
) {}