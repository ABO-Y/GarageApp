package com.nust.garageapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nust.garageapp.data.entity.*
import com.nust.garageapp.data.repository.GarageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GarageViewModel(private val repository: GarageRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<Employee?>(null)
    val currentUser: StateFlow<Employee?> = _currentUser.asStateFlow()

    val allTrucks: Flow<List<Truck>> = repository.allTrucks
    val allCheckIns: Flow<List<CheckInRecord>> = repository.allCheckIns
    val allEmployees: Flow<List<Employee>> = repository.allEmployees

    fun login(name: String, onResult: (Boolean) -> Unit) {
        if (name.lowercase() == "guest") {
            _currentUser.value = Employee(id = -1, name = "Guest", role = UserRole.GUEST)
            onResult(true)
            return
        }

        viewModelScope.launch {
            val employee = repository.getEmployeeByName(name)
            if (employee != null) {
                _currentUser.value = employee
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun addTruck(licensePlate: String, model: String, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val existingTruck = repository.getTruckByPlate(licensePlate)
            if (existingTruck != null) {
                onResult(existingTruck.id)
            } else {
                val id = repository.addTruck(Truck(licensePlate = licensePlate, model = model))
                onResult(id)
            }
        }
    }

    fun checkInTruck(truckId: Long, kilometers: Int, condition: String, rating: Int) {
        val mechanicId = _currentUser.value?.id ?: 0
        viewModelScope.launch {
            repository.addCheckIn(
                CheckInRecord(
                    truckId = truckId,
                    mechanicId = mechanicId,
                    checkInDate = System.currentTimeMillis(),
                    kilometers = kilometers,
                    condition = condition,
                    rating = rating
                )
            )
        }
    }

    fun addEmployee(name: String, role: UserRole = UserRole.MECHANIC) {
        viewModelScope.launch {
            repository.addEmployee(Employee(name = name, role = role))
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
