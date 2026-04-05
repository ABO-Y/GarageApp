package com.nust.garageapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nust.garageapp.data.dao.GarageDao
import com.nust.garageapp.data.entity.CheckInRecord
import com.nust.garageapp.data.entity.Employee
import com.nust.garageapp.data.entity.RepairTask
import com.nust.garageapp.data.entity.Truck

@Database(
    entities = [Truck::class, CheckInRecord::class, Employee::class, RepairTask::class],
    version = 1,
    exportSchema = false
)
abstract class GarageDatabase : RoomDatabase() {
    abstract fun garageDao(): GarageDao

    companion object {
        @Volatile
        private var Instance: GarageDatabase? = null

        fun getDatabase(context: Context): GarageDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GarageDatabase::class.java, "garage_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
