package com.example.shkudikweatherapp.pojo.weather

import com.google.gson.annotations.SerializedName

data class WindModel constructor(@SerializedName("speed")
                                 val speed: Float,
                                 @SerializedName("deg")
                                 val deg: Int) {}