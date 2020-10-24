package com.example.shkudikweatherapp.data_layer.pojo.weather

import com.google.gson.annotations.SerializedName

data class WeatherModel constructor(@SerializedName("id")
                                    val id: Int,
                                    @SerializedName("main")
                                    val main: String,
                                    @SerializedName("description")
                                    val description: String,
                                    @SerializedName("icon")
                                    val icon: String) {}