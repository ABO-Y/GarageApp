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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    viewModel: GarageViewModel,
    onBack: () -> Unit,
) {
    var licensePlate by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var kilometers by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(3) }
    
    val trucks by viewModel.allTrucks.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Truck Check-in") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            Text("Register New Truck / Check-in", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = licensePlate,
                onValueChange = { licensePlate = it },
                label = { Text("License Plate") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Truck Model") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = kilometers,
                onValueChange = { kilometers = it },
                label = { Text("Kilometers Driven") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = condition,
                onValueChange = { condition = it },
                label = { Text("Vehicle Condition") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Mechanic Rating (1-5):", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = rating.toFloat(),
                onValueChange = { rating = it.toInt() },
                valueRange = 1f..5f,
                steps = 3,
            )
            Text("Rating: $rating", style = MaterialTheme.typography.bodySmall)
            
            Button(
                onClick = {
                    if (licensePlate.isNotBlank() && kilometers.isNotBlank()) {
                        viewModel.addTruck(licensePlate, model) { truckId ->
                            viewModel.checkInTruck(
                                truckId = truckId, 
                                kilometers = kilometers.toIntOrNull() ?: 0, 
                                condition = condition,
                                rating = rating,
                            )
                            onBack()
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            ) {
                Text("Complete Check-in")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Existing Trucks", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(trucks) { truck ->
                    ListItem(
                        headlineContent = { Text(truck.licensePlate) },
                        supportingContent = { Text(truck.model) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
