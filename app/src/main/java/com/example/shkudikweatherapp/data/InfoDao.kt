package com.example.shkudikweatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface InfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addInfo(info: Info)

    @Query(value = "SELECT * FROM info")
    fun readAllData(): LiveData<List<Info>>

}