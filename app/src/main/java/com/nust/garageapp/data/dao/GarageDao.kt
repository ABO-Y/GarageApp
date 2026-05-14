package com.nust.garageapp.data.dao

import androidx.room.*
import com.nust.garageapp.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GarageDao {
    // Truck
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: Truck): Long

    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): Flow<List<Truck>>

    @Query("SELECT * FROM trucks WHERE licensePlate = :licensePlate LIMIT 1")
    suspend fun getTruckByPlate(licensePlate: String): Truck?

    // CheckInRecord
    @Insert
    suspend fun insertCheckIn(record: CheckInRecord): Long

    @Query("SELECT * FROM check_in_records ORDER BY checkInDate DESC")
    fun getAllCheckIns(): Flow<List<CheckInRecord>>

    @Query("SELECT * FROM check_in_records WHERE truckId = :truckId")
    fun getCheckInsForTruck(truckId: Long): Flow<List<CheckInRecord>>

    // Employee
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee): Long

    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE name = :name LIMIT 1")
    suspend fun getEmployeeByName(name: String): Employee?

    @Delete
    suspend fun deleteEmployee(employee: Employee)

    // RepairTask
    @Insert
    suspend fun insertRepairTask(task: RepairTask): Long

    @Update
    suspend fun updateRepairTask(task: RepairTask): Int

    @Query("SELECT * FROM repair_tasks WHERE checkInRecordId = :checkInId")
    fun getTasksForCheckIn(checkInId: Long): Flow<List<RepairTask>>

    @Query("SELECT * FROM repair_tasks WHERE employeeId = :employeeId")
    fun getTasksByEmployee(employeeId: Long): Flow<List<RepairTask>>
}
