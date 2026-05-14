package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "trucks",
    indices = [androidx.room.Index(value = ["licensePlate"], unique = true)]
)
data class Truck(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val licensePlate: String,
    val model: String
)
