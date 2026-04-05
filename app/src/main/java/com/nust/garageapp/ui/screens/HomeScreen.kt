package com.nust.garageapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToCheckIn: () -> Unit,
    onNavigateToRepair: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Valentine's Garage",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = onNavigateToCheckIn,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Truck Check-in")
        }
        
        Button(
            onClick = onNavigateToRepair,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Repair & Maintenance")
        }
        
        Button(
            onClick = onNavigateToReports,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("View Reports")
        }
    }
}
