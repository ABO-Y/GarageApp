package com.nust.garageapp.data.database

import androidx.room.TypeConverter
import com.nust.garageapp.data.entity.UserRole

/**
 * Type converters for Room to handle non-primitive types like Enums.
 */
class Converters {
    /**
     * Converts a [UserRole] enum to its String representation for database storage.
     */
    @TypeConverter
    fun fromUserRole(value: UserRole): String {
        return value.name
    }

    /**
     * Converts a String value from the database back into a [UserRole] enum.
     */
    @TypeConverter
    fun toUserRole(value: String): UserRole {
        return UserRole.valueOf(value)
    }
}
