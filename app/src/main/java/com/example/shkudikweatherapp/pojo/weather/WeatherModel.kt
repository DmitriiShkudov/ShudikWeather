package com.example.shkudikweatherapp.pojo.weather

data class WeatherModel constructor(val id: Int,
                                    val main: String,
                                    val description: String,
                                    val icon: String) {}