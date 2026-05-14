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

/**
 * The entry point activity for the Garage Application.
 * Responsible for initializing the database, repository, and ViewModel, 
 * and setting up the Jetpack Compose UI content.
 */
class MainActivity : ComponentActivity() {
    /** Lazy-initialized database instance. */
    private val database by lazy { GarageDatabase.getDatabase(this) }
    /** Lazy-initialized repository instance. */
    private val repository by lazy { GarageRepository(database.garageDao()) }
    /** ViewModel instance scoped to this activity, initialized with a custom factory. */
    private val viewModel: GarageViewModel by viewModels {
        GarageViewModelFactory(repository)
    }

    /**
     * Called when the activity is first created.
     * Sets up the edge-to-edge display and the Compose UI.
     */
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

/**
 * Root Composable function for the entire application.
 * Manages navigation between different screens using a [NavHost].
 * 
 * @param viewModel The shared ViewModel instance used by all screens.
 */
@Composable
fun GarageApp(viewModel: GarageViewModel) {
    /** The navigation controller used to switch between destinations. */
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding),
        ) {
            /** Destination for the initial login screen. */
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                ) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            /** Destination for the main dashboard screen. */
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
            /** Destination for the staff management screen (Manager only). */
            composable("manage_staff") {
                ManageStaffScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
            /** Destination for the vehicle check-in screen. */
            composable("check_in") {
                CheckInScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
            /** Destination for the repair task management screen. */
            composable("repair") {
                RepairScreen(
                    viewModel = viewModel,
                ) {
                    navController.popBackStack()
                }
            }
            /** Destination for viewing various application reports. */
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
