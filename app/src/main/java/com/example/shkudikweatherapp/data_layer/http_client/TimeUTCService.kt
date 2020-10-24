package com.example.shkudikweatherapp.data_layer.http_client

import com.example.shkudikweatherapp.data_layer.pojo.time_utc.TimeUTC
import retrofit2.Call
import retrofit2.http.GET


interface TimeUTCService {

    @GET("/api/json/utc/now")
    fun getTimeUTC(): Call<TimeUTC>

}