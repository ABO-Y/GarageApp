package com.nust.garageapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nust.garageapp.data.entity.UserRole
import com.nust.garageapp.ui.GarageViewModel

/**
 * The main dashboard screen shown after a successful login.
 * Displays different action buttons based on the user's role (Manager, Mechanic, or Guest).
 * 
 * @param viewModel The shared application ViewModel.
 * @param onNavigateToCheckIn Callback to go to the Check-in screen.
 * @param onNavigateToRepair Callback to go to the Repair screen.
 * @param onNavigateToReports Callback to go to the Reports screen.
 * @param onNavigateToManageStaff Callback to go to the Staff Management screen.
 * @param onLogout Callback to log out and return to the login screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: GarageViewModel,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToRepair: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToManageStaff: () -> Unit,
    onLogout: () -> Unit,
) {
    /** The current user session observed from the ViewModel. */
    val currentUser by viewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Valentine's Garage") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, ${currentUser?.name ?: "Guest"}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Role: ${currentUser?.role ?: "Guest"}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            val role = currentUser?.role ?: UserRole.GUEST

            // Mechanics can register trucks
            if ((role == UserRole.MECHANIC) || (role == UserRole.MANAGER)) {
                Button(
                    onClick = onNavigateToCheckIn,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                ) {
                    Text("Truck Check-in")
                }
            }
            
            // Mechanics and Managers can see/do repairs
            if ((role == UserRole.MECHANIC) || (role == UserRole.MANAGER)) {
                Button(
                    onClick = onNavigateToRepair,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                ) {
                    Text("Repair & Maintenance")
                }
            }
            
            // Managers and Guests can see reports
            if ((role == UserRole.MANAGER) || (role == UserRole.GUEST)) {
                Button(
                    onClick = onNavigateToReports,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                ) {
                    Text("View Reports")
                }
            }

            // ONLY Managers can add staff
            if (role == UserRole.MANAGER) {
                Button(
                    onClick = onNavigateToManageStaff,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                ) {
                    Text("Manage Staff (Add Mechanics)")
                }
            }
        }
    }
}
