package com.example.shkudikweatherapp.pojo.weather

import com.google.gson.annotations.SerializedName

class MainModel constructor(@SerializedName("temp")
                            val temp: Float,
                            @SerializedName("feels_like")
                            val feels_like: Float,
                            @SerializedName("temp_min")
                            val temp_min: Float,
                            @SerializedName("temp_max")
                            val temp_max: Float,
                            @SerializedName("pressure")
                            val pressure: Int,
                            @SerializedName("humidity")
                            val humidity: Int) {}