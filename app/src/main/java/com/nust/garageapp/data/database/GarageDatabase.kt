package com.nust.garageapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nust.garageapp.data.dao.GarageDao
import com.nust.garageapp.data.entity.CheckInRecord
import com.nust.garageapp.data.entity.Employee
import com.nust.garageapp.data.entity.RepairTask
import com.nust.garageapp.data.entity.Truck

/**
 * The main Room database class for the Garage Application.
 * Defines the entities, versioning, and provides access to the DAO.
 */
@Database(
    entities = [Truck::class, CheckInRecord::class, Employee::class, RepairTask::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GarageDatabase : RoomDatabase() {
    /** Provides access to the [GarageDao] for database operations. */
    abstract fun garageDao(): GarageDao

    companion object {
        /** Singleton instance to prevent multiple instances of the database being opened. */
        @Volatile
        private var Instance: GarageDatabase? = null

        /**
         * Returns the singleton instance of the [GarageDatabase], creating it if necessary.
         * 
         * @param context The application context.
         * @return The [GarageDatabase] instance.
         */
        fun getDatabase(context: Context): GarageDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, GarageDatabase::class.java, "garage_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
