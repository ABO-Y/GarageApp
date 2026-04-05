package com.nust.garageapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nust.garageapp.data.entity.CheckInRecord
import com.nust.garageapp.data.entity.Employee
import com.nust.garageapp.data.entity.RepairTask
import com.nust.garageapp.ui.GarageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairScreen(
    viewModel: GarageViewModel,
    onBack: () -> Unit
) {
    val checkIns by viewModel.allCheckIns.collectAsState(initial = emptyList())
    val employees by viewModel.allEmployees.collectAsState(initial = emptyList())
    
    var selectedCheckIn by remember { mutableStateOf<CheckInRecord?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedCheckIn == null) "Select Vehicle" else "Repair Tasks") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedCheckIn == null) onBack() else selectedCheckIn = null
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (selectedCheckIn == null) {
                // List of vehicles in the garage
                Text("Current Vehicles in Garage", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(checkIns) { record ->
                        Card(
                            onClick = { selectedCheckIn = record },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            ListItem(
                                headlineContent = { Text("Truck ID: ${record.truckId}") },
                                supportingContent = { Text("Checked in: ${java.util.Date(record.checkInDate)}") }
                            )
                        }
                    }
                }
            } else {
                // Tasks for the selected vehicle
                val tasks by viewModel.getTasksForCheckIn(selectedCheckIn!!.id).collectAsState(initial = emptyList())
                var newTaskDescription by remember { mutableStateOf("") }
                
                Text("Manage Tasks for ID: ${selectedCheckIn!!.id}", style = MaterialTheme.typography.titleMedium)
                
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        label = { Text("New Task") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (newTaskDescription.isNotBlank()) {
                                viewModel.addTask(selectedCheckIn!!.id, newTaskDescription)
                                newTaskDescription = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add")
                    }
                }
                
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(task, employees, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: RepairTask, employees: List<Employee>, viewModel: GarageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(task.notes) }
    var selectedEmployeeId by remember { mutableStateOf<Long?>(task.employeeId) }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { if (!task.isCompleted) showDialog = true }
                )
                Column {
                    Text(task.description, style = MaterialTheme.typography.bodyLarge)
                    if (task.isCompleted) {
                        Text("Completed by Employee ID: ${task.employeeId}", style = MaterialTheme.typography.bodySmall)
                        Text("Notes: ${task.notes}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Complete Task") },
            text = {
                Column {
                    Text("Select your name and add notes:")
                    // Simple text field for name if employee list is empty for now
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Repair Notes") }
                    )
                    // In a real app, this would be a DropdownMenu of employees
                    Text("Employee ID (Mock):")
                    OutlinedTextField(
                        value = (selectedEmployeeId ?: "").toString(),
                        onValueChange = { selectedEmployeeId = it.toLongOrNull() },
                        label = { Text("Your ID") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedEmployeeId != null) {
                        viewModel.completeTask(task, selectedEmployeeId!!, notes)
                        showDialog = false
                    }
                }) {
                    Text("Done")
                }
            }
        )
    }
}
