package com.nust.garageapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    MANAGER,
    MECHANIC,
    GUEST
}

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val role: UserRole = UserRole.MECHANIC
)
