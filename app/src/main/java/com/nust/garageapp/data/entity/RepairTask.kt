package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "repair_tasks",
    foreignKeys = [
        ForeignKey(
            entity = CheckInRecord::class,
            parentColumns = ["id"],
            childColumns = ["checkInRecordId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class RepairTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val checkInRecordId: Long,
    val description: String,
    val isCompleted: Boolean = false,
    val notes: String = "",
    val employeeId: Long? = null,
    val completionDate: Long? = null
)
