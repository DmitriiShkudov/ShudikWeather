package com.example.shkudikweatherapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info")
data class Info(@PrimaryKey(autoGenerate = true)
                val id: Int = 0,
                @ColumnInfo(name = "city")
                val city: String,
                @ColumnInfo(name = "date")
                val date: String,
                @ColumnInfo(name = "description")
                val description: String) {


    override fun toString() = "{ id = " + this.id + "; city = " +  this.city +  "; desc = " + this.description + " }"


}