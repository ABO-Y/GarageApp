package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "check_in_records",
    foreignKeys = [
        ForeignKey(
            entity = Truck::class,
            parentColumns = ["id"],
            childColumns = ["truckId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CheckInRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val truckId: Long,
    val checkInDate: Long, // timestamp
    val kilometers: Int,
    val condition: String
)
