package com.example.medipal.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object AddMedicineFlow : Screen("add_medicine_flow") // Đây là một flow, chứa các màn hình con
}