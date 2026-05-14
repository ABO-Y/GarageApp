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
import com.nust.garageapp.data.entity.RepairTask
import com.nust.garageapp.ui.GarageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairScreen(
    viewModel: GarageViewModel,
    onBack: () -> Unit,
) {
    val checkIns by viewModel.allCheckIns.collectAsState(initial = emptyList())
    val dateFormat = remember { java.text.SimpleDateFormat("dd MMM yyyy HH:mm", java.util.Locale.getDefault()) }
    
    var selectedCheckIn by remember { mutableStateOf<CheckInRecord?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedCheckIn == null) "Select Vehicle" else "Repair Tasks") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedCheckIn == null) onBack() else selectedCheckIn = null
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
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
                                supportingContent = { Text("Checked in: ${dateFormat.format(java.util.Date(record.checkInDate))}") }
                            )
                        }
                    }
                }
            } else {
                // Tasks for the selected vehicle
                val tasks by remember(selectedCheckIn?.id) {
                    viewModel.getTasksForCheckIn(selectedCheckIn!!.id)
                }.collectAsState(initial = emptyList())
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
                        TaskItem(task, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: RepairTask, viewModel: GarageViewModel) {
    var showDialog by remember { mutableStateOf(value = false) }
    var notes by remember { mutableStateOf(task.notes) }
    val currentUser by viewModel.currentUser.collectAsState()

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
                        Text("Completed by: ${task.employeeId}", style = MaterialTheme.typography.bodySmall)
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
                    Text("Describe what you worked on:")
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Repair Notes") },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Working as: ${currentUser?.name ?: "Guest"}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val workerId = currentUser?.id ?: -1
                        viewModel.completeTask(task, workerId, notes)
                        showDialog = false
                    },
                ) {
                    Text("Done")
                }
            },
        )
    }
}
