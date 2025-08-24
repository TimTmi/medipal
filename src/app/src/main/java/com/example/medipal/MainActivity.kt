package com.example.medipal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.usecase.GetScheduledEventsUseCase

import  com.example.medipal.presentation.ui.theme.MediPalTheme
import androidx.core.view.WindowCompat
import com.example.medipal.presentation.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kích hoạt chế độ edge-to-edge để giao diện vẽ ra sau các thanh hệ thống
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MediPalTheme {
                // MainScreen sẽ quản lý tất cả Scaffold và Navigation.
                MainScreen()
            }
        }
    }
}