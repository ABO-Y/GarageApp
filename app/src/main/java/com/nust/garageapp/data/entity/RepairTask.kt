package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a specific repair action or maintenance item assigned to a vehicle visit.
 * 
 * @property id The unique identifier for the repair task.
 * @property checkInRecordId Foreign key referencing the specific [CheckInRecord] this task belongs to.
 * @property description Details of what needs to be fixed or maintained.
 * @property isCompleted Flag indicating if the task has been finished.
 * @property notes Additional observations or technical details added upon completion.
 * @property employeeId Foreign key referencing the [Employee] who completed the task.
 * @property completionDate The timestamp (milliseconds) when the task was marked as completed.
 */
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
