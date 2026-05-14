package com.nust.garageapp.data.database

import androidx.room.TypeConverter
import com.nust.garageapp.data.entity.UserRole

class Converters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String {
        return value.name
    }

    @TypeConverter
    fun toUserRole(value: String): UserRole {
        return UserRole.valueOf(value)
    }
}
