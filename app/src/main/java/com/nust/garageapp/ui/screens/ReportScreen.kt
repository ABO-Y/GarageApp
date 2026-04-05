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
import com.nust.garageapp.ui.GarageViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: GarageViewModel,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Check-ins", "Employee Work")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
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
    val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    LazyColumn(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        items(checkIns) { record ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Truck ID: ${record.truckId}", style = MaterialTheme.typography.titleMedium)
                    Text("Date: ${dateFormat.format(Date(record.checkInDate))}")
                    Text("Kilometers: ${record.kilometers}")
                    Text("Condition: ${record.condition}")
                }
            }
        }
    }
}

@Composable
fun EmployeeReport(viewModel: GarageViewModel) {
    val employees by viewModel.allEmployees.collectAsState(initial = emptyList())
    var selectedEmployeeId by remember { mutableStateOf<Long?>(null) }
    var showAddEmployee by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Button(onClick = { showAddEmployee = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Add New Employee")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (employees.isEmpty()) {
            Text("No employees registered yet.")
        } else {
            Text("Select Employee to view tasks:", style = MaterialTheme.typography.labelLarge)
            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                items(employees) { employee ->
                    FilterChip(
                        selected = selectedEmployeeId == employee.id,
                        onClick = { selectedEmployeeId = employee.id },
                        label = { Text(employee.name) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            selectedEmployeeId?.let { empId ->
                val employeeTasks by viewModel.getTasksByEmployee(empId).collectAsState(initial = emptyList())
                Text("Tasks Completed by Employee:", style = MaterialTheme.typography.titleSmall)
                if (employeeTasks.isEmpty()) {
                    Text("No tasks completed yet.", modifier = Modifier.padding(top = 8.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(employeeTasks) { task ->
                            ListItem(
                                headlineContent = { Text(task.description) },
                                supportingContent = { Text("Notes: ${task.notes}") }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddEmployee) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddEmployee = false },
            title = { Text("Add Employee") },
            text = {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addEmployee(name)
                        showAddEmployee = false
                    }
                }) { Text("Add") }
            }
        )
    }
}
