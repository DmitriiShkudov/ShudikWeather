package com.example.shkudikweatherapp.pojo.countries

data class Result constructor(val id: Int,
                              val name: String,
                              val iso: String,
                              val iso3: String,
                              val isoNumeric: String,
                              val fips: String,
                              val continent: String,
                              val currencyCode: String,
                              val phonePrefix: ArrayList<String>,
                              val postalCodeFormat: String,
                              val postalCodeRegex: String,
                              val languages: ArrayList<String>,
                              val externalIds: ExternalIds,
                              val localizedNames: LocalizedNames) {
}