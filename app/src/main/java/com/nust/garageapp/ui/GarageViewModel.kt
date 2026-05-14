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

/**
 * Main ViewModel for the Garage Application.
 * Manages the state and business logic for all screens including login, check-in, repairs, and staff management.
 * 
 * @property repository The data source provider for all database operations.
 */
class GarageViewModel(private val repository: GarageRepository) : ViewModel() {

    /** Internal state for the currently logged-in user. */
    private val _currentUser = MutableStateFlow<Employee?>(null)
    /** Public read-only state for the currently logged-in user. */
    val currentUser: StateFlow<Employee?> = _currentUser.asStateFlow()

    /** Flow of all trucks currently in the system. */
    val allTrucks: Flow<List<Truck>> = repository.allTrucks
    /** Flow of all check-in records recorded in the system. */
    val allCheckIns: Flow<List<CheckInRecord>> = repository.allCheckIns
    /** Flow of all registered employees. */
    val allEmployees: Flow<List<Employee>> = repository.allEmployees

    /**
     * Attempts to log in a user by their name.
     * Special handling for "Guest" and "Valentine" (Manager).
     * 
     * @param name The name entered by the user.
     * @param onResult Callback invoked with true if login succeeds, false otherwise.
     */
    fun login(name: String, onResult: (Boolean) -> Unit) {
        if (name.lowercase() == "guest") {
            _currentUser.value = Employee(id = -1, name = "Guest", role = UserRole.GUEST)
            onResult(true)
            return
        }

        // Auto-create Valentine if she doesn't exist
        if (name.equals("Valentine", ignoreCase = true)) {
            viewModelScope.launch {
                val existing = repository.getEmployeeByName("Valentine")
                if (existing == null) {
                    val id = repository.addEmployee(Employee(name = "Valentine", role = UserRole.MANAGER))
                    _currentUser.value = Employee(id = id, name = "Valentine", role = UserRole.MANAGER)
                } else {
                    _currentUser.value = existing
                }
                onResult(true)
            }
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

    /** Clears the current user session. */
    fun logout() {
        _currentUser.value = null
    }

    /**
     * Registers a new truck if it doesn't exist, otherwise returns the existing truck ID.
     * 
     * @param licensePlate The unique license plate.
     * @param model The vehicle model.
     * @param onResult Callback with the ID of the truck.
     */
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

    /**
     * Creates a new check-in record for a vehicle.
     * 
     * @param truckId The ID of the truck being checked in.
     * @param kilometers Current odometer reading.
     * @param condition Description of vehicle state.
     * @param rating Mechanic's assessment rating.
     */
    fun checkInTruck(truckId: Long, kilometers: Int, condition: String, rating: Int) {
        val user = _currentUser.value
        // If user is a Guest (ID -1) or not found, we use null for the mechanicId
        val mechanicId = if (user != null && user.id > 0) user.id else null
        
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

    /** Adds a new employee to the staff list. */
    fun addEmployee(name: String, role: UserRole = UserRole.MECHANIC) {
        viewModelScope.launch {
            repository.addEmployee(Employee(name = name, role = role))
        }
    }

    /** Deletes an employee from the staff list. */
    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.deleteEmployee(employee)
        }
    }

    /** Retrieves all tasks for a specific check-in ID. */
    fun getTasksForCheckIn(checkInId: Long): Flow<List<RepairTask>> {
        return repository.getTasksForCheckIn(checkInId)
    }

    /** Retrieves all tasks handled by a specific employee. */
    fun getTasksByEmployee(employeeId: Long): Flow<List<RepairTask>> {
        return repository.getTasksByEmployee(employeeId)
    }

    /** Adds a pending repair task to a check-in record. */
    fun addTask(checkInId: Long, description: String) {
        viewModelScope.launch {
            repository.addRepairTask(RepairTask(checkInRecordId = checkInId, description = description))
        }
    }

    /**
     * Marks a repair task as completed with notes.
     * 
     * @param task The task to update.
     * @param employeeId The ID of the employee completing the task.
     * @param notes Technical notes about the repair.
     */
    fun completeTask(task: RepairTask, employeeId: Long, notes: String) {
        // If employeeId is -1 (Guest), use null to avoid Foreign Key violations
        val idToSave = if (employeeId > 0) employeeId else null
        
        viewModelScope.launch {
            repository.updateRepairTask(
                task.copy(
                    isCompleted = true,
                    employeeId = idToSave,
                    notes = notes,
                    completionDate = System.currentTimeMillis()
                )
            )
        }
    }
}

/**
 * Factory class to create instances of [GarageViewModel] with the necessary repository.
 */
class GarageViewModelFactory(private val repository: GarageRepository) : ViewModelProvider.Factory {
    /** Creates a new instance of the requested ViewModel class. */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GarageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GarageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
