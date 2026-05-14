package com.nust.garageapp.data.dao

import androidx.room.*
import com.nust.garageapp.data.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) providing the primary interface for database operations.
 * Handles CRUD operations for Trucks, Employees, Check-ins, and Repair Tasks.
 */
@Dao
interface GarageDao {
    /**
     * Inserts a new truck into the database or replaces it if a conflict occurs.
     * @param truck The truck entity to insert.
     * @return The row ID of the newly inserted truck.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: Truck): Long

    /**
     * Retrieves all trucks registered in the system.
     * @return A [Flow] containing the list of all trucks, updated in real-time.
     */
    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): Flow<List<Truck>>

    /**
     * Finds a truck by its license plate number.
     * @param licensePlate The license plate string to search for.
     * @return The [Truck] object if found, null otherwise.
     */
    @Query("SELECT * FROM trucks WHERE licensePlate = :licensePlate LIMIT 1")
    suspend fun getTruckByPlate(licensePlate: String): Truck?

    /**
     * Records a new vehicle check-in.
     * @param record The check-in record details.
     * @return The row ID of the new record.
     */
    @Insert
    suspend fun insertCheckIn(record: CheckInRecord): Long

    /**
     * Retrieves all check-in records sorted by date (newest first).
     * @return A [Flow] of check-in records.
     */
    @Query("SELECT * FROM check_in_records ORDER BY checkInDate DESC")
    fun getAllCheckIns(): Flow<List<CheckInRecord>>

    /**
     * Retrieves all check-ins associated with a specific truck.
     * @param truckId The ID of the truck.
     * @return A [Flow] of check-in records for that truck.
     */
    @Query("SELECT * FROM check_in_records WHERE truckId = :truckId")
    fun getCheckInsForTruck(truckId: Long): Flow<List<CheckInRecord>>

    /**
     * Registers a new employee or staff member.
     * @param employee The employee details.
     * @return The row ID of the new employee.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee): Long

    /**
     * Retrieves all staff members.
     * @return A [Flow] of all employees.
     */
    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<Employee>>

    /**
     * Finds an employee by their exact name.
     * @param name The name to search for.
     * @return The [Employee] if found, null otherwise.
     */
    @Query("SELECT * FROM employees WHERE name = :name LIMIT 1")
    suspend fun getEmployeeByName(name: String): Employee?

    /**
     * Removes an employee from the database.
     * @param employee The employee object to delete.
     * @return The number of rows affected (should be 1).
     */
    @Delete
    suspend fun deleteEmployee(employee: Employee): Int

    /**
     * Adds a new repair task to a check-in record.
     * @param task The repair task to add.
     * @return The row ID of the new task.
     */
    @Insert
    suspend fun insertRepairTask(task: RepairTask): Long

    /**
     * Updates an existing repair task (e.g., marking as completed).
     * @param task The task object with updated values.
     * @return The number of rows updated.
     */
    @Update
    suspend fun updateRepairTask(task: RepairTask): Int

    /**
     * Retrieves all repair tasks for a specific check-in session.
     * @param checkInId The ID of the check-in record.
     * @return A [Flow] of tasks for that visit.
     */
    @Query("SELECT * FROM repair_tasks WHERE checkInRecordId = :checkInId")
    fun getTasksForCheckIn(checkInId: Long): Flow<List<RepairTask>>

    /**
     * Retrieves all tasks assigned to or completed by a specific employee.
     * @param employeeId The ID of the employee.
     * @return A [Flow] of tasks associated with the employee.
     */
    @Query("SELECT * FROM repair_tasks WHERE employeeId = :employeeId")
    fun getTasksByEmployee(employeeId: Long): Flow<List<RepairTask>>
}
