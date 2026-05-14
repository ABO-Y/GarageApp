package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines the different access levels and roles for users in the system.
 */
enum class UserRole {
    /** Administrative role with access to staff management and reports. */
    MANAGER,
    /** Technical role focused on performing and recording repairs. */
    MECHANIC,
    /** Restricted role for viewing basic information. */
    GUEST
}

/**
 * Represents a staff member or user entity in the database.
 * 
 * @property id The unique identifier for the employee, auto-generated.
 * @property name The full name of the employee, used for identification and login.
 * @property role The functional role assigned to the employee, determining permissions.
 */
@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val role: UserRole = UserRole.MECHANIC
)
