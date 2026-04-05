package com.nust.garageapp.data.repository

import com.nust.garageapp.data.dao.GarageDao
import com.nust.garageapp.data.entity.*
import kotlinx.coroutines.flow.Flow

class GarageRepository(private val garageDao: GarageDao) {
    // Trucks
    val allTrucks: Flow<List<Truck>> = garageDao.getAllTrucks()
    suspend fun addTruck(truck: Truck): Long = garageDao.insertTruck(truck)

    // Check-ins
    val allCheckIns: Flow<List<CheckInRecord>> = garageDao.getAllCheckIns()
    suspend fun addCheckIn(record: CheckInRecord): Long = garageDao.insertCheckIn(record)
    fun getCheckInsForTruck(truckId: Long): Flow<List<CheckInRecord>> = garageDao.getCheckInsForTruck(truckId)

    // Employees
    val allEmployees: Flow<List<Employee>> = garageDao.getAllEmployees()
    suspend fun addEmployee(employee: Employee): Long = garageDao.insertEmployee(employee)

    // Tasks
    fun getTasksForCheckIn(checkInId: Long): Flow<List<RepairTask>> = garageDao.getTasksForCheckIn(checkInId)
    suspend fun addRepairTask(task: RepairTask) = garageDao.insertRepairTask(task)
    suspend fun updateRepairTask(task: RepairTask) = garageDao.updateRepairTask(task)
    fun getTasksByEmployee(employeeId: Long): Flow<List<RepairTask>> = garageDao.getTasksByEmployee(employeeId)
}
