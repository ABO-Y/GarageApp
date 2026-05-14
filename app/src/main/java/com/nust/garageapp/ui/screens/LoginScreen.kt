package com.nust.garageapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nust.garageapp.data.entity.UserRole
import com.nust.garageapp.ui.GarageViewModel

/**
 * The initial entry screen for the application.
 * Handles user authentication via name entry.
 * 
 * @param viewModel The shared application ViewModel.
 * @param onLoginSuccess Callback invoked after a successful login.
 */
@Composable
fun LoginScreen(
    viewModel: GarageViewModel,
    onLoginSuccess: () -> Unit,
) {
    /** State for the user's name input field. */
    var name by remember { mutableStateOf("") }
    /** State for displaying error messages during login attempts. */
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name (or 'Guest')") },
            modifier = Modifier.fillMaxWidth(),
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    viewModel.login(name) { success ->
                        if (success) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "User not found. Try 'Valentine' or 'Guest'"
                        }
                    }
                } else {
                    errorMessage = "Please enter your name"
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Login")
        }
    }
}
