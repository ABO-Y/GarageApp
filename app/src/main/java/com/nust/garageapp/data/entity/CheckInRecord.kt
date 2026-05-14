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
        ),
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["mechanicId"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ]
)
data class CheckInRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val truckId: Long,
    val mechanicId: Long = 0, // ID of employee who did check-in
    val checkInDate: Long, // timestamp
    val kilometers: Int,
    val condition: String,
    val rating: Int = 0 // New: Rating given by mechanic (1-5)
)
