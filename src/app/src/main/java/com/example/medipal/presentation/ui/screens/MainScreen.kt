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
import com.example.medipal.presentation.ui.components.InAppNotificationBottomSheet
import com.example.medipal.presentation.viewmodel.AddMedicationViewModel
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
import com.example.medipal.presentation.viewmodel.AddAppointmentViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel
import com.example.medipal.presentation.viewmodel.NotificationViewModel
import com.example.medipal.domain.service.InAppNotificationManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

//    val application = LocalContext.current.applicationContext as MediPalApplication
//    val viewModelFactory = ViewModelFactory(application.container)

    val homeViewModel: HomeViewModel = koinViewModel()
    val addMedicationViewModel: AddMedicationViewModel = koinViewModel()
    val addHealthcareReminderViewModel: AddHealthcareReminderViewModel = koinViewModel()
    val addAppointmentViewModel: AddAppointmentViewModel = koinViewModel()
    val notificationViewModel: NotificationViewModel = koinViewModel()

    // In-app notification state
    var currentNotification by remember { mutableStateOf<com.example.medipal.domain.model.NotificationItem?>(null) }
    
    // Get today's notification count for badge
    val notificationUiState by notificationViewModel.uiState.collectAsState()
    val todayNotificationCount = notificationUiState.todayNotifications.size
    
    // Debug logging
    LaunchedEffect(todayNotificationCount) {
        println("DEBUG MainScreen: todayNotificationCount = $todayNotificationCount")
        println("DEBUG MainScreen: todayNotifications = ${notificationUiState.todayNotifications.map { it.title }}")
    }
    
    // Listen for in-app notifications
    LaunchedEffect(Unit) {
        InAppNotificationManager.notificationFlow.collect { notification ->
            currentNotification = notification
        }
    }

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

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.AddMedicineFlow.route) {
                BottomTabBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    todayNotificationCount = todayNotificationCount
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
        
        // In-app notification bottom sheet
        InAppNotificationBottomSheet(
            notification = currentNotification,
            onDismiss = { currentNotification = null },
            onTaken = {
                currentNotification?.let { notification ->
                    notificationViewModel.markAsTaken(notification.id)
                }
                currentNotification = null
            },
            onSkipped = {
                currentNotification?.let { notification ->
                    notificationViewModel.markAsSkipped(notification.id)
                }
                currentNotification = null
            }
        )
    }
}