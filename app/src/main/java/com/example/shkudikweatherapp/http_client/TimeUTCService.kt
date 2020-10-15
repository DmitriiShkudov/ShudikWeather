package com.example.shkudikweatherapp.http_client

import com.example.shkudikweatherapp.pojo.time_utc.TimeUTC
import retrofit2.Call
import retrofit2.http.GET


interface TimeUTCService {

    @GET("/api/json/utc/now")
    fun getTimeUTC(): Call<TimeUTC>

}