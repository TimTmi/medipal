// Trong file MainScreen.kt
package com.example.medipal.presentation.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
//import com.example.medipal.MediPalApplication
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.ui.components.BottomTabBar
import com.example.medipal.presentation.viewmodel.AddMedicationViewModel
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
import com.example.medipal.presentation.viewmodel.AddAppointmentViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel
import com.example.medipal.presentation.viewmodel.AuthViewModel
import com.example.medipal.presentation.viewmodel.AuthState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = GlobalContext.get().get<AuthViewModel>()
    val homeViewModel: HomeViewModel = koinViewModel()
    val addMedicationViewModel: AddMedicationViewModel = koinViewModel()
    val addHealthcareReminderViewModel: AddHealthcareReminderViewModel = koinViewModel()
    val addAppointmentViewModel: AddAppointmentViewModel = koinViewModel()

    // Check authentication state
    LaunchedEffect(Unit) {
        authViewModel.checkAuthState()
    }

    val authState by authViewModel.authState.collectAsState()

    // THAY ĐỔI QUAN TRỌNG: Thêm mã để điều khiển màu sắc icon trên status bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        // SideEffect sẽ chạy sau mỗi lần composition thành công
        SideEffect {
            val window = (view.context as Activity).window
            // Đặt isAppearanceLightStatusBars = true để các icon (pin, giờ, v.v.) chuyển sang màu TỐI
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // Show loading screen while checking auth state
    if (authState is AuthState.Initial) {
        LoadingScreen()
    } else if (authState is AuthState.Unauthenticated) {
        AuthScreen(navController = navController, viewModel = authViewModel)
    } else {
        Scaffold(
            bottomBar = {
                if (currentRoute != Screen.AddMedicineFlow.route) {
                    BottomTabBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController, viewModel = homeViewModel)
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(navController = navController)
            }
            composable(Screen.Medications.route) {
                MedicationsScreen(
                    navController = navController
                )
            }
            composable(Screen.Notifications.route) {
                NotificationsScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(Screen.AddMedicineFlow.route) {
                AddMedicineFlow(
                    mainNavController = navController,
                    viewModel = addMedicationViewModel
                )
            }
            composable(Screen.AddHealthcareReminderFlow.route) {
                AddHealthcareReminderFlow(
                    mainNavController = navController,
                    viewModel = addHealthcareReminderViewModel
                )
            }
            composable(Screen.AddAppointmentFlow.route) {
                AddAppointmentFlow(
                    mainNavController = navController,
                    viewModel = addAppointmentViewModel
                )
            }
            composable(Screen.HistoryLog.route) {
                HistoryLogScreen(navController = navController)
            }
            composable(Screen.MissedDoseDetail.route) { backStackEntry ->
                val notificationId = backStackEntry.arguments?.getString("notificationId") ?: ""
                MissedDoseDetailScreen(
                    navController = navController,
                    notificationId = notificationId
                )
            }
            composable(Screen.UpcomingDoseDetail.route) { backStackEntry ->
                val notificationId = backStackEntry.arguments?.getString("notificationId") ?: ""
                UpcomingDoseDetailScreen(
                    navController = navController,
                    notificationId = notificationId
                )
            }
            composable(Screen.EditProfile.route) {
                EditProfileScreen(navController = navController)
            }
        }
    }
}
}