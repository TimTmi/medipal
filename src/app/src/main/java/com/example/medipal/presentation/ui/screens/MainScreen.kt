package com.example.medipal.presentation.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medipal.domain.model.AccountType
import com.example.medipal.domain.repository.CaregiverAssignmentRepository
import com.example.medipal.domain.service.AccountService
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.ui.components.BottomTabBar
import com.example.medipal.presentation.ui.components.InAppNotificationBottomSheet
import com.example.medipal.presentation.viewmodel.*
import com.example.medipal.presentation.viewmodel.AddMedicationViewModel
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
import com.example.medipal.presentation.viewmodel.AddAppointmentViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel
import com.example.medipal.presentation.viewmodel.NotificationViewModel
import com.example.medipal.domain.model.NotificationStatus
import com.example.medipal.domain.service.InAppNotificationManager
import com.example.medipal.presentation.viewmodel.AppointmentsViewModel
import com.example.medipal.presentation.viewmodel.RemindersViewModel
import com.example.medipal.presentation.viewmodel.AuthViewModel
import com.example.medipal.presentation.viewmodel.AuthState
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = koinViewModel()
    val profileRepositoryManager: ProfileRepositoryManager = koinInject()
    val caregiverAssignmentRepository: CaregiverAssignmentRepository = koinInject()

    // In-app notification state
    var currentNotification by remember { mutableStateOf<com.example.medipal.domain.model.NotificationItem?>(null) }
    
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
    val account = (authState as? AuthState.Authenticated)?.account
    val accountService: AccountService = koinInject()


    // Show loading screen while checking auth state
    if (authState is AuthState.Initial) {
        LoadingScreen()
        return
    } else if (authState is AuthState.Unauthenticated) {
        AuthScreen(navController = navController, viewModel = authViewModel)
        return
    }

    // Only create ViewModels after authentication is confirmed
    val homeViewModel: HomeViewModel = koinViewModel()
    val addMedicationViewModel: AddMedicationViewModel = koinViewModel()
    val addHealthcareReminderViewModel: AddHealthcareReminderViewModel = koinViewModel()
    val addAppointmentViewModel: AddAppointmentViewModel = koinViewModel()
    val notificationViewModel: NotificationViewModel = koinViewModel()
    
    // Get today's notification count for badge
    val notificationUiState by notificationViewModel.uiState.collectAsState()
    val todayNotificationCount = notificationUiState.todayNotifications.count { notification ->
        notification.status == NotificationStatus.MISSED || notification.status == NotificationStatus.UPCOMING
    }
    
    // Debug logging
    LaunchedEffect(todayNotificationCount) {
        println("DEBUG MainScreen: todayNotificationCount = $todayNotificationCount")
        println("DEBUG MainScreen: todayNotifications = ${notificationUiState.todayNotifications.map { it.title }}")
    }

    // THAY ĐỔI QUAN TRỌNG: Thêm mã để điều khiển màu sắc icon trên status bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    var expanded by remember { mutableStateOf(false) }

    // Profile data for caregivers
    var profileIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var profileNames by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    if (account?.type == AccountType.CAREGIVER) {
        val caregiverAssignments by caregiverAssignmentRepository
            .getAssignmentsForCaregiver(account.id)
            .collectAsState(initial = emptyList())

        LaunchedEffect(caregiverAssignments) {
            val accounts = caregiverAssignments
                .map { it.customerId }
                .map { id -> async { accountService.getAccount(id) } }
                .awaitAll()
                .filterNotNull()

            profileIds = accounts.map { it.profileId }.filterNotNull()

            // Fetch profile names
            val names = accounts.associate { account ->
                val profile = async { accountService.getProfile(account.profileId) }
                account.profileId to (profile.await()?.fullName ?: "Unknown")
            }
            profileNames = names
        }
    }

    Box {

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
            composable(Screen.ManageCaregiverAccess.route) {
                val viewModel: ManageCaregiverViewModel = koinViewModel()
                ManageCaregiverAccessScreen(navController, viewModel)
            }
            composable(Screen.ProfileSelection.route) {
                ProfileSelectionScreen(navController = navController, account = account)
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

        // Profile selection button for caregivers (floating above bottom bar)
        // Only show on main screens: Home, Medications, Appointments, Notifications
        val showProfileButton = currentRoute in listOf(
            Screen.Home.route,
            Screen.Medications.route,
            Screen.AppointmentReminder.route,
            Screen.Notifications.route
        )

        if (account?.type == AccountType.CAREGIVER && profileNames.isNotEmpty() && showProfileButton) {
            val currentProfileId = profileRepositoryManager.getCurrentProfileId()
            val currentProfileName = profileNames[currentProfileId] ?: "Select Profile"

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp, end = 80.dp), // Space above bottom bar and align with FAB
                contentAlignment = Alignment.BottomStart
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navController.navigate(Screen.ProfileSelection.route)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Switch Profile",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Current Profile",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = currentProfileName,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
    }
}
