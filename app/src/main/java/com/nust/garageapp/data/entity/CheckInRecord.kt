package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a record of a truck entering the garage for service.
 * Linked to a specific Truck and the Employee who performed the intake.
 * 
 * @property id The unique identifier for the check-in record.
 * @property truckId Foreign key referencing the [Truck] that was checked in.
 * @property mechanicId Foreign key referencing the [Employee] who processed the intake.
 * @property checkInDate The timestamp (milliseconds) when the check-in occurred.
 * @property kilometers The current odometer reading of the truck at the time of entry.
 * @property condition A descriptive summary of the truck's physical or mechanical state.
 * @property rating A subjective quality score (1-5) provided by the intake mechanic.
 */
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
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class CheckInRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val truckId: Long,
    val mechanicId: Long? = null, // ID of employee who did check-in (nullable for guests)
    val checkInDate: Long, // timestamp
    val kilometers: Int,
    val condition: String,
    val rating: Int = 0 // New: Rating given by mechanic (1-5)
)
