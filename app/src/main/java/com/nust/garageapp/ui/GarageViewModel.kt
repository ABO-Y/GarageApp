package com.nust.garageapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nust.garageapp.data.entity.*
import com.nust.garageapp.data.repository.GarageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GarageViewModel(private val repository: GarageRepository) : ViewModel() {

    val allTrucks: Flow<List<Truck>> = repository.allTrucks
    val allCheckIns: Flow<List<CheckInRecord>> = repository.allCheckIns
    val allEmployees: Flow<List<Employee>> = repository.allEmployees

    fun addTruck(licensePlate: String, model: String, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.addTruck(Truck(licensePlate = licensePlate, model = model))
            onResult(id)
        }
    }

    fun checkInTruck(truckId: Long, kilometers: Int, condition: String) {
        viewModelScope.launch {
            repository.addCheckIn(
                CheckInRecord(
                    truckId = truckId,
                    checkInDate = System.currentTimeMillis(),
                    kilometers = kilometers,
                    condition = condition
                )
            )
        }
    }

    fun addEmployee(name: String) {
        viewModelScope.launch {
            repository.addEmployee(Employee(name = name))
        }
    }

    fun getTasksForCheckIn(checkInId: Long): Flow<List<RepairTask>> {
        return repository.getTasksForCheckIn(checkInId)
    }

    fun getTasksByEmployee(employeeId: Long): Flow<List<RepairTask>> {
        return repository.getTasksByEmployee(employeeId)
    }

    fun addTask(checkInId: Long, description: String) {
        viewModelScope.launch {
            repository.addRepairTask(RepairTask(checkInRecordId = checkInId, description = description))
        }
    }

    fun completeTask(task: RepairTask, employeeId: Long, notes: String) {
        viewModelScope.launch {
            repository.updateRepairTask(
                task.copy(
                    isCompleted = true,
                    employeeId = employeeId,
                    notes = notes,
                    completionDate = System.currentTimeMillis()
                )
            )
        }
    }
}

class GarageViewModelFactory(private val repository: GarageRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GarageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GarageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
