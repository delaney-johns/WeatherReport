package com.ait.weatherreport.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true) var cityId: Long?,
    @ColumnInfo(name = "name") var name: String
) : Serializable
//need serializable to send the object