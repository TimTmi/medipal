package com.example.medipal.presentation.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.ui.components.BottomTabBar
import com.example.medipal.presentation.ui.components.InAppNotificationBottomSheet
import com.example.medipal.presentation.viewmodel.*
import com.example.medipal.presentation.viewmodel.AddMedicationViewModel
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
import com.example.medipal.presentation.viewmodel.AddAppointmentViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel
import com.example.medipal.presentation.viewmodel.NotificationViewModel
import com.example.medipal.domain.service.InAppNotificationManager
import com.example.medipal.presentation.viewmodel.AppointmentsViewModel
import com.example.medipal.presentation.viewmodel.RemindersViewModel
import com.example.medipal.presentation.viewmodel.AuthViewModel
import com.example.medipal.presentation.viewmodel.AuthState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = koinViewModel()
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

    // Check authentication state
    LaunchedEffect(Unit) {
        authViewModel.checkAuthState()
    }

    val authState by authViewModel.authState.collectAsState()

    // Show loading screen while checking auth state
    if (authState is AuthState.Initial) {
        LoadingScreen()
        return
    } else if (authState is AuthState.Unauthenticated) {
        AuthScreen(navController = navController, viewModel = authViewModel)
        return
    }

    // THAY ĐỔI QUAN TRỌNG: Thêm mã để điều khiển màu sắc icon trên status bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        bottomBar = {
            // Logic ẩn BottomBar (giữ nguyên)
            val showBottomBar = currentRoute in listOf(
                Screen.Home.route,
                Screen.AppointmentReminder.route,
                Screen.Medications.route,
                Screen.Notifications.route,
                Screen.Profile.route
            )
            if (showBottomBar) {
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
                val homeViewModel: HomeViewModel = koinViewModel()
                HomeScreen(navController = navController, viewModel = homeViewModel)
            }
            composable(Screen.AppointmentReminder.route) {
                AppointmentReminderScreen(navController = navController)
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
                val addMedicationViewModel: AddMedicationViewModel = koinViewModel()
                AddMedicineFlow(
                    mainNavController = navController,
                    viewModel = addMedicationViewModel
                )
            }
            composable(Screen.AddHealthcareReminderFlow.route) {
                val addHealthcareReminderViewModel: AddHealthcareReminderViewModel = koinViewModel()
                AddHealthcareReminderFlow(
                    mainNavController = navController,
                    viewModel = addHealthcareReminderViewModel
                )
            }
            composable(Screen.AddAppointmentFlow.route) {
                val addAppointmentViewModel: AddAppointmentViewModel = koinViewModel()
                AddAppointmentFlow(
                    mainNavController = navController,
                    viewModel = addAppointmentViewModel
                )
            }
            composable(Screen.HistoryLog.route) {
                HistoryLogScreen(navController = navController)
            }


            // --- CÁC ROUTE MỚI, SỬ DỤNG CÙNG MỘT FACTORY ---
            composable(
                route = Screen.MedicineDetail.route,
                arguments = listOf(navArgument("medicationId") { type = NavType.StringType })
            ) {
                // SỬA LỖI Ở ĐÂY: Dùng factory đã được tạo
                val viewModel: MedicationDetailViewModel = koinViewModel()
                MedicineDetailScreen(navController = navController, viewModel = viewModel)
            }

            composable(
                route = Screen.EditMedicine.route,
                arguments = listOf(navArgument("medicationId") { type = NavType.StringType })
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                // SỬA LỖI Ở ĐÂY: Dùng lại factory, nhưng với owner là màn hình cha
                val viewModel: MedicationDetailViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
                EditMedicineScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.EditMedicineFrequency.route) { backStackEntry ->
                // Chia sẻ ViewModel với màn hình Detail/Edit
                // Chúng ta phải lấy lại backStackEntry của nguồn, tức là màn hình Detail
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }

                // Yêu cầu cung cấp cùng một instance ViewModel mà màn hình Detail và Edit đang sử dụng
                val viewModel: MedicationDetailViewModel = koinViewModel(viewModelStoreOwner = parentEntry)

                SelectFrequencyScreen(navController = navController, viewModel = viewModel)
            }

            // Các màn hình con cho edit frequency
            composable(Screen.EditMedicineXDays.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                val viewModel: MedicationDetailViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
                EditMedicineXDaysScreen(navController = navController, viewModel = viewModel)
            }

            composable(Screen.EditMedicineSpecificDays.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                val viewModel: MedicationDetailViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
                EditMedicineSpecificDaysScreen(navController = navController, viewModel = viewModel)
            }

            composable(Screen.EditMedicineXWeeks.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                val viewModel: MedicationDetailViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
                EditMedicineXWeeksScreen(navController = navController, viewModel = viewModel)
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
            composable(Screen.Appointments.route) {
                AppointmentsScreen(navController = navController)
            }
            composable(Screen.Reminders.route) {
                RemindersScreen(navController = navController)
            }
            composable(Screen.AddOptions.route) {
                AddOptionsScreen(navController = navController)
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
