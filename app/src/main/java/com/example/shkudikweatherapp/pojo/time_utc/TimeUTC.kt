package com.example.shkudikweatherapp.pojo.time_utc

import com.google.gson.annotations.SerializedName

data class TimeUTC constructor(@SerializedName("$" + "id")
                               val id: String,
                               @SerializedName("currentDateTime")
                               val currentDateTime: String,
                               @SerializedName("utcOffset")
                               val utcOffset: String,
                               @SerializedName("isDayLightSavingsTime")
                               val isDayLightSavingsTime: Boolean,
                               @SerializedName("dayOfTheWeek")
                               val dayOfTheWeek: String,
                               @SerializedName("timeZoneName")
                               val timeZoneName: String,
                               @SerializedName("currentFileTime")
                               val currentFileTime: Long,
                               @SerializedName("ordinalDate")
                               val ordinalDate: String,
                               @SerializedName("serviceResponse")
                               val serviceResponse: String?) {}