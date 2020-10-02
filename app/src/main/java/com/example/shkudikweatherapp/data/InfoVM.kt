package com.example.shkudikweatherapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class InfoVM(application: Application) : AndroidViewModel(application) {

    private val allData: LiveData<List<Info>>
    private val repository: InfoRepository

    init {

        val infoDao = InfoDatabase.getDatabase(application).infoDao()
        repository = InfoRepository(infoDao)
        allData = repository.allData

    }

    fun addUser(info: Info) = repository.addInfo(info)

}