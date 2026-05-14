package com.nust.garageapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nust.garageapp.data.database.GarageDatabase
import com.nust.garageapp.data.repository.GarageRepository
import com.nust.garageapp.ui.GarageViewModel
import com.nust.garageapp.ui.GarageViewModelFactory
import com.nust.garageapp.ui.screens.*
import com.nust.garageapp.ui.theme.GarageAppTheme

class MainActivity : ComponentActivity() {
    private val database by lazy { GarageDatabase.getDatabase(this) }
    private val repository by lazy { GarageRepository(database.garageDao()) }
    private val viewModel: GarageViewModel by viewModels {
        GarageViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GarageAppTheme {
                GarageApp(viewModel)
            }
        }
    }
}

@Composable
fun GarageApp(viewModel: GarageViewModel) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                ) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToCheckIn = { navController.navigate("check_in") },
                    onNavigateToRepair = { navController.navigate("repair") },
                    onNavigateToReports = { navController.navigate("reports") },
                    onNavigateToManageStaff = { navController.navigate("manage_staff") },
                    onLogout = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
            composable("manage_staff") {
                ManageStaffScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
            composable("check_in") {
                CheckInScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
            composable("repair") {
                RepairScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
            composable("reports") {
                ReportScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}
