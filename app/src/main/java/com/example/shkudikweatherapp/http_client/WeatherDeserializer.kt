package com.example.shkudikweatherapp.http_client

import com.example.shkudikweatherapp.pojo.weather.Weather
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeatherDeserializer : JsonDeserializer<Weather> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Weather {

        val content = json!!.asJsonObject.get()
        return Gson().fromJson(content, typeOfT)

    }
}