package com.example.shkudikweatherapp.data_layer.states

enum class MainDescription(val string: String, val icon: String) {

    THUNDERSTORM("Thunderstorm", "11d"), DRIZZLE("Drizzle", "50d"),
    RAIN("Rain", "10d"), CLEAR("Clear", "01d"), MIST("Mist", "50d"),
    SMOKE("Smoke", "50d"), HAZE("Haze", "50d"), DUST("Dust", "50n"),
    FOG("Fog", "50d"), SAND("Sand", "50d"), ASH("Ash", "50d"),
    SQUALL("Squall", "11d"), TORNADO("Tornado", "11d"),
    CLOUDS("Clouds", "03d"), SNOW("Snow", "13d"),

    THUNDERSTORM_NIGHT("Thunderstorm", "11n"), DRIZZLE_NIGHT("Drizzle", "50n"),
    RAIN_NIGHT("Rain", "10n"), CLEAR_NIGHT("Clear", "01n"),
    MIST_NIGHT("Mist", "50n"), SMOKE_NIGHT("Smoke", "50n"),
    HAZE_NIGHT("Haze", "50n"), DUST_NIGHT("Dust", "50n"),
    FOG_NIGHT("Fog", "50n"), SAND_NIGHT("Sand", "50n"),
    ASH_NIGHT("Ash", "50n"), SQUALL_NIGHT("Squall", "11n"),
    TORNADO_NIGHT("Tornado", "11n"), CLOUDS_NIGHT("Clouds","03n"),
    SNOW_NIGHT("Snow", "13n")

}

