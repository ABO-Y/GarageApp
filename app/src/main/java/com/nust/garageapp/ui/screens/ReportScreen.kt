package com.nust.garageapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nust.garageapp.ui.GarageViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: GarageViewModel,
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Check-ins", "Employee Work")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Garage Reports") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            when (selectedTab) {
                0 -> CheckInReport(viewModel)
                1 -> EmployeeReport(viewModel)
            }
        }
    }
}

@Composable
fun CheckInReport(viewModel: GarageViewModel) {
    val checkIns by viewModel.allCheckIns.collectAsState(initial = emptyList())
    val employees by viewModel.allEmployees.collectAsState(initial = emptyList())
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()) }

    LazyColumn(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        items(checkIns) { record ->
            val mechanic = employees.find { it.id == record.mechanicId }
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Truck ID: ${record.truckId}", style = MaterialTheme.typography.titleMedium)
                    Text("Date: ${dateFormat.format(Date(record.checkInDate))}")
                    Text("Checked in by: ${mechanic?.name ?: "Unknown (ID: ${record.mechanicId})"}")
                    Text("Kilometers: ${record.kilometers}")
                    Text("Condition: ${record.condition}")
                    Text("Mechanic Rating: ${record.rating}/5", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun EmployeeReport(viewModel: GarageViewModel) {
    val employees by viewModel.allEmployees.collectAsState(initial = emptyList())
    var selectedEmployeeId by remember { mutableStateOf<Long?>(null) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        if (employees.isEmpty()) {
            Text("No employees registered yet.")
        } else {
            Text("Select Employee to view completed tasks:", style = MaterialTheme.typography.labelLarge)
            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                items(employees) { employee ->
                    FilterChip(
                        selected = selectedEmployeeId == employee.id,
                        onClick = { selectedEmployeeId = employee.id },
                        label = { Text("${employee.name} (${employee.role})") },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            selectedEmployeeId?.let { empId ->
                val employeeTasks by remember(empId) {
                    viewModel.getTasksByEmployee(empId)
                }.collectAsState(initial = emptyList())
                val currentEmployee = employees.find { it.id == empId }
                
                Text("Work Log for ${currentEmployee?.name}:", style = MaterialTheme.typography.titleSmall)
                if (employeeTasks.isEmpty()) {
                    Text("No tasks recorded for this employee.", modifier = Modifier.padding(top = 8.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(employeeTasks) { task ->
                            ListItem(
                                headlineContent = { Text(task.description) },
                                supportingContent = { 
                                    Column {
                                        Text("Truck Visit ID: ${task.checkInRecordId}")
                                        Text("Notes: ${task.notes}")
                                        if (task.completionDate != null) {
                                            Text("Completed: ${dateFormat.format(Date(task.completionDate))}")
                                        }
                                    }
                                },
                                trailingContent = { 
                                    if (task.isCompleted) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
