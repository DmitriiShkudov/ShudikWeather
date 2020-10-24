package com.example.shkudikweatherapp.data_layer.pojo.forecast

import com.google.gson.annotations.SerializedName

data class Forecast constructor(@SerializedName("list")
                                val list: List<ListModel>) {
}