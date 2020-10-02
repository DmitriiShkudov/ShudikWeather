package com.example.shkudikweatherapp.data

class InfoRepository(private val infoDao: InfoDao) {


    val allData = infoDao.readAllData()

    fun addInfo(info: Info) = infoDao.addInfo(info)


}