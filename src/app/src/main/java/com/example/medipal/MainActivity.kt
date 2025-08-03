package com.example.medipal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medipal.data.repository.FakeAppointmentRepositoryImpl
import com.example.medipal.data.repository.FakeMedicationRepositoryImpl
import com.example.medipal.data.repository.FakeReminderRepositoryImpl
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.usecase.GetScheduledEventsUseCase
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.ui.screens.HomeScreen
import com.example.medipal.presentation.ui.screens.AddMedicineFlow
import com.example.medipal.presentation.viewmodel.AddMedicineViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel

import  com.example.medipal.presentation.ui.theme.MediPalTheme


class MainActivity : ComponentActivity() {

    // --- Giả lập Dependency Injection ---
    // Trong dự án thực tế, bạn sẽ dùng Hilt hoặc Koin để cung cấp các đối tượng này
    private val medicationRepository: MedicationRepository = FakeMedicationRepositoryImpl()
    private val appointmentRepository: AppointmentRepository = FakeAppointmentRepositoryImpl()
    private val reminderRepository: ReminderRepository = FakeReminderRepositoryImpl()
    private val getScheduledEventsUseCase by lazy { GetScheduledEventsUseCase(medicationRepository, appointmentRepository, reminderRepository) }
    private val addMedicationUseCase by lazy { AddMedicationUseCase(medicationRepository) }
    // --- Kết thúc giả lập DI ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Tên Theme có thể khác nhau tùy theo bạn đặt tên project
            // Ví dụ: YourAppNameTheme
            MediPalTheme {
                AppNavigation()
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.Home.route) {

            // Route cho màn hình chính (Home)
            composable(Screen.Home.route) {
                // Cung cấp ViewModel cho HomeScreen
                val homeViewModel: HomeViewModel = viewModel(
                    factory = ViewModelFactory { HomeViewModel(getScheduledEventsUseCase) }
                )
                HomeScreen(navController = navController, viewModel = homeViewModel)
            }

            // Route cho toàn bộ luồng thêm thuốc
            composable(Screen.AddMedicineFlow.route) {
                // Cung cấp ViewModel cho luồng thêm thuốc
                val addMedicineViewModel: AddMedicineViewModel = viewModel(
                    factory = ViewModelFactory { AddMedicineViewModel(addMedicationUseCase) }
                )
                AddMedicineFlow(mainNavController = navController, viewModel = addMedicineViewModel)
            }
        }
    }
}

/**
 * Một ViewModelFactory đơn giản để "tiêm" UseCase vào ViewModel.
 * Giúp chúng ta không cần dùng thư viện DI phức tạp trong ví dụ này.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory<T : ViewModel>(private val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}