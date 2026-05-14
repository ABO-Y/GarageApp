package com.nust.garageapp.data.repository

import com.nust.garageapp.data.dao.GarageDao
import com.nust.garageapp.data.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to multiple data sources.
 * It provides a clean API for the UI to interact with the [GarageDao].
 * 
 * @property garageDao The data access object used to perform database operations.
 */
class GarageRepository(private val garageDao: GarageDao) {
    /** Observable flow of all trucks in the database. */
    val allTrucks: Flow<List<Truck>> = garageDao.getAllTrucks()
    
    /** Adds a new truck record. */
    suspend fun addTruck(truck: Truck): Long = garageDao.insertTruck(truck)
    
    /** Searches for a truck by license plate. */
    suspend fun getTruckByPlate(plate: String): Truck? = garageDao.getTruckByPlate(plate)

    /** Observable flow of all check-in records. */
    val allCheckIns: Flow<List<CheckInRecord>> = garageDao.getAllCheckIns()
    
    /** Creates a new check-in entry. */
    suspend fun addCheckIn(record: CheckInRecord): Long = garageDao.insertCheckIn(record)
    
    /** Gets history for a specific truck. */
    fun getCheckInsForTruck(truckId: Long): Flow<List<CheckInRecord>> = garageDao.getCheckInsForTruck(truckId)

    /** Observable flow of all staff members. */
    val allEmployees: Flow<List<Employee>> = garageDao.getAllEmployees()
    
    /** Registers a new staff member. */
    suspend fun addEmployee(employee: Employee): Long = garageDao.insertEmployee(employee)
    
    /** Finds a staff member by name. */
    suspend fun getEmployeeByName(name: String): Employee? = garageDao.getEmployeeByName(name)
    
    /** Removes a staff member. */
    suspend fun deleteEmployee(employee: Employee) = garageDao.deleteEmployee(employee)

    /** Gets repair tasks for a specific vehicle visit. */
    fun getTasksForCheckIn(checkInId: Long): Flow<List<RepairTask>> = garageDao.getTasksForCheckIn(checkInId)
    
    /** Adds a new task to be performed. */
    suspend fun addRepairTask(task: RepairTask) = garageDao.insertRepairTask(task)
    
    /** Updates task status or completion details. */
    suspend fun updateRepairTask(task: RepairTask) = garageDao.updateRepairTask(task)
    
    /** Gets task history for a specific mechanic. */
    fun getTasksByEmployee(employeeId: Long): Flow<List<RepairTask>> = garageDao.getTasksByEmployee(employeeId)
}
