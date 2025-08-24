//// Trong file MainScreen.kt
//package com.example.medipal.presentation.ui.screens
//
//import android.app.Activity
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalView
//import androidx.core.view.WindowCompat
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.medipal.MediPalApplication
//import com.example.medipal.presentation.ui.components.BottomTabBar
//import com.example.medipal.presentation.viewmodel.AddMedicationViewModel
//import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
//import com.example.medipal.presentation.viewmodel.AddAppointmentViewModel
//import com.example.medipal.presentation.viewmodel.HomeViewModel
//import com.example.medipal.presentation.viewmodel.ViewModelFactory
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.medipal.presentation.navigation.Screen
//import com.example.medipal.presentation.viewmodel.MedicationDetailViewModel
//
//@Composable
//fun MainScreen() {
//    val navController = rememberNavController()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    val application = LocalContext.current.applicationContext as MediPalApplication
//    val viewModelFactory = ViewModelFactory(application.container)
//
//    val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
//    val addMedicationViewModel: AddMedicationViewModel = viewModel(factory = viewModelFactory)
//    val addHealthcareReminderViewModel: AddHealthcareReminderViewModel = viewModel(factory = viewModelFactory)
//    val addAppointmentViewModel: AddAppointmentViewModel = viewModel(factory = viewModelFactory)
//
//    // THAY ĐỔI QUAN TRỌNG: Thêm mã để điều khiển màu sắc icon trên status bar
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        // SideEffect sẽ chạy sau mỗi lần composition thành công
//        SideEffect {
//            val window = (view.context as Activity).window
//            // Đặt isAppearanceLightStatusBars = true để các icon (pin, giờ, v.v.) chuyển sang màu TỐI
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
//        }
//    }
//
//    Scaffold(
//        bottomBar = {
//            if (currentRoute != Screen.AddMedicineFlow.route) {
//                BottomTabBar(
//                    navController = navController,
//                    currentRoute = currentRoute
//                )
//            }
//        }
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = Screen.Home.route,
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable(Screen.Home.route) {
//                HomeScreen(navController = navController, viewModel = homeViewModel)
//            }
//            composable(Screen.Calendar.route) {
//                CalendarScreen(navController = navController)
//            }
//            composable(Screen.Medications.route) {
//                MedicationsScreen(
//                    navController = navController,
//                    viewModelFactory = viewModelFactory
//                )
//            }
//            composable(Screen.Notifications.route) {
//                NotificationsScreen(navController = navController)
//            }
//            composable(Screen.Profile.route) {
//                ProfileScreen(navController = navController)
//            }
//            composable(Screen.AddMedicineFlow.route) {
//                AddMedicineFlow(
//                    mainNavController = navController,
//                    viewModel = addMedicationViewModel
//                )
//            }
//            composable(Screen.AddHealthcareReminderFlow.route) {
//                AddHealthcareReminderFlow(
//                    mainNavController = navController,
//                    viewModel = addHealthcareReminderViewModel
//                )
//            }
//            composable(Screen.AddAppointmentFlow.route) {
//                AddAppointmentFlow(
//                    mainNavController = navController,
//                    viewModel = addAppointmentViewModel
//                )
//            }
//            composable(Screen.HistoryLog.route) {
//                HistoryLogScreen(navController = navController)
//            }
//            composable(
//                route = Screen.MedicineDetail.route,
//                arguments = listOf(navArgument("medicationId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                // Sử dụng viewModel() với factory để khởi tạo
//                val viewModel: MedicationDetailViewModel = viewModel(
//                    factory = ViewModelFactory {
//                        // Bạn cần cung cấp các UseCase ở đây.
//                        // Đây là phần khó khi không dùng Hilt.
//                        // Tạm thời để trống, chúng ta sẽ cần truyền UseCase từ MainActivity vào.
//                        MedicationDetailViewModel(
//                            savedStateHandle = backStackEntry.savedStateHandle,
//                            // getMedicationByIdUseCase = ...,
//                            // updateMedicationUseCase = ...,
//                            // deleteMedicationUseCase = ...
//                        )
//                    }
//                )
//                MedicineDetailScreen(navController = navController, viewModel = viewModel)
//            }
//
//
//            composable(
//                route = Screen.EditMedicine.route,
//                arguments = listOf(navArgument("medicationId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                // Chia sẻ ViewModel với màn hình cha
//                val parentEntry = remember(backStackEntry) {
//                    navController.getBackStackEntry(Screen.MedicineDetail.route)
//                }
//                val viewModel: MedicationDetailViewModel = viewModel(
//                    viewModelStoreOwner = parentEntry,
//                    factory = ViewModelFactory {
//                        MedicationDetailViewModel(
//                            savedStateHandle = parentEntry.savedStateHandle,
//                            // getMedicationByIdUseCase = ...,
//                            // updateMedicationUseCase = ...,
//                            // deleteMedicationUseCase = ...
//                        )
//                    }
//                )
//                EditMedicineScreen(navController = navController, viewModel = viewModel)
//            }
//
//        }
//    }
//}




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
import com.example.medipal.MediPalApplication
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.ui.components.BottomTabBar
import com.example.medipal.presentation.viewmodel.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
// --- KHỞI TẠO FACTORY MỘT LẦN DUY NHẤT ---
    val application = LocalContext.current.applicationContext as MediPalApplication
    val viewModelFactory = remember {
        // Chỉ cần truyền vào 'container'
        ViewModelFactory(application.container)
    }
// --- KẾT THÚC KHỞI TẠO ---

// Thay đổi màu status bar (giữ nguyên)
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
                Screen.Calendar.route,
                Screen.Medications.route,
                Screen.Notifications.route,
                Screen.Profile.route
            )
            if (showBottomBar) {
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
            // --- CÁC ROUTE CŨ, SỬ DỤNG CÙNG MỘT FACTORY ---
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                HomeScreen(navController = navController, viewModel = homeViewModel)
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(navController = navController)
            }
            composable(Screen.Medications.route) {
                MedicationsScreen(
                    navController = navController,
                    viewModelFactory = viewModelFactory
                )
            }
            composable(Screen.Notifications.route) {
                NotificationsScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(Screen.AddMedicineFlow.route) {
                val addMedicationViewModel: AddMedicationViewModel = viewModel(factory = viewModelFactory)
                AddMedicineFlow(
                    mainNavController = navController,
                    viewModel = addMedicationViewModel
                )
            }
            composable(Screen.AddHealthcareReminderFlow.route) {
                val addHealthcareReminderViewModel: AddHealthcareReminderViewModel = viewModel(factory = viewModelFactory)
                AddHealthcareReminderFlow(
                    mainNavController = navController,
                    viewModel = addHealthcareReminderViewModel
                )
            }
            composable(Screen.AddAppointmentFlow.route) {
                val addAppointmentViewModel: AddAppointmentViewModel = viewModel(factory = viewModelFactory)
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
                val viewModel: MedicationDetailViewModel = viewModel(factory = viewModelFactory)
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
                val viewModel: MedicationDetailViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = viewModelFactory
                )
                EditMedicineScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.EditMedicineFrequency.route) { backStackEntry ->
                // Chia sẻ ViewModel với màn hình Detail/Edit
                // Chúng ta phải lấy lại backStackEntry của nguồn, tức là màn hình Detail
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }

                // Yêu cầu cung cấp cùng một instance ViewModel mà màn hình Detail và Edit đang sử dụng
                val viewModel: MedicationDetailViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = viewModelFactory
                )

                SelectFrequencyScreen(navController = navController, viewModel = viewModel)
            }
            
            // Các màn hình con cho edit frequency
            composable(Screen.EditMedicineXDays.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                val viewModel: MedicationDetailViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = viewModelFactory
                )
                EditMedicineXDaysScreen(navController = navController, viewModel = viewModel)
            }
            
            composable(Screen.EditMedicineSpecificDays.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                val viewModel: MedicationDetailViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = viewModelFactory
                )
                EditMedicineSpecificDaysScreen(navController = navController, viewModel = viewModel)
            }
            
            composable(Screen.EditMedicineXWeeks.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.MedicineDetail.route)
                }
                val viewModel: MedicationDetailViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = viewModelFactory
                )
                EditMedicineXWeeksScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}