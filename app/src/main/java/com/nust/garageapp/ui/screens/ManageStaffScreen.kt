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
import com.nust.garageapp.data.entity.UserRole
import com.nust.garageapp.ui.GarageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageStaffScreen(
    viewModel: GarageViewModel,
    onBack: () -> Unit,
) {
    var newEmployeeName by remember { mutableStateOf("") }
    val employees by viewModel.allEmployees.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Staff") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Add New Mechanic", style = MaterialTheme.typography.titleMedium)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = newEmployeeName,
                    onValueChange = { newEmployeeName = it },
                    label = { Text("Mechanic Name") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (newEmployeeName.isNotBlank()) {
                            viewModel.addEmployee(newEmployeeName, UserRole.MECHANIC)
                            newEmployeeName = ""
                        }
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(56.dp)
                ) {
                    Text("Add")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Current Employees", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(employees) { employee ->
                    ListItem(
                        headlineContent = { Text(employee.name) },
                        supportingContent = { Text("Role: ${employee.role}") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
