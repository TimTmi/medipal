package com.example.medipal.presentation.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth_screen")
    object Home : Screen("home_screen")
    object Calendar : Screen("calendar_screen")
    object Medications : Screen("medications_screen")
    object Notifications : Screen("notifications_screen")
    object Profile : Screen("profile_screen")
    object EditProfile : Screen("edit_profile")
    object AddMedicineFlow : Screen("add_medicine_flow") // Đây là một flow, chứa các màn hình con
    object AddHealthcareReminderFlow : Screen("add_healthcare_reminder_flow") // Flow thêm healthcare reminder
    object AddAppointmentFlow : Screen("add_appointment_flow") // Flow thêm cuộc hẹn
    object HistoryLog : Screen("history_log") // Màn hình lịch sử
    object MissedDoseDetail : Screen("missed_dose_detail/{notificationId}") {
        fun createRoute(notificationId: String) = "missed_dose_detail/$notificationId"
    }
    object UpcomingDoseDetail : Screen("upcoming_dose_detail/{notificationId}") {
        fun createRoute(notificationId: String) = "upcoming_dose_detail/$notificationId"
    }
}