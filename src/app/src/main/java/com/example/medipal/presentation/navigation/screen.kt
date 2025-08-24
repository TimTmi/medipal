package com.example.medipal.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object AppointmentReminder : Screen("appointment_reminder_screen")
    object Medications : Screen("medications_screen")
    object Notifications : Screen("notifications_screen")
    object Profile : Screen("profile_screen")
    object EditProfile : Screen("edit_profile")
    object AddMedicineFlow : Screen("add_medicine_flow") // Đây là một flow, chứa các màn hình con
    object AddHealthcareReminderFlow : Screen("add_healthcare_reminder_flow") // Flow thêm healthcare reminder
    object AddAppointmentFlow : Screen("add_appointment_flow") // Flow thêm cuộc hẹn
    object HistoryLog : Screen("history_log") // Màn hình lịch sử
    object Appointments : Screen("appointments_screen") // Màn hình danh sách appointments
    object Reminders : Screen("reminders_screen") // Màn hình danh sách reminders
    object AddOptions : Screen("add_options_screen") // Màn hình lựa chọn thêm mới
    object MissedDoseDetail : Screen("missed_dose_detail/{notificationId}") {
        fun createRoute(notificationId: String) = "missed_dose_detail/$notificationId"
    }
    object UpcomingDoseDetail : Screen("upcoming_dose_detail/{notificationId}") {
        fun createRoute(notificationId: String) = "upcoming_dose_detail/$notificationId"
    }
    object MedicineDetail : Screen("medicine_detail/{medicationId}") {
        fun createRoute(medicationId: String) = "medicine_detail/$medicationId"
    }
    object EditMedicine : Screen("edit_medicine/{medicationId}") {
        fun createRoute(medicationId: String) = "edit_medicine/$medicationId"
    }
    object EditMedicineFrequency : Screen("edit_medicine_frequency")
    object EditMedicineXDays : Screen("edit_medicine_x_days")
    object EditMedicineSpecificDays : Screen("edit_medicine_specific_days")
    object EditMedicineXWeeks : Screen("edit_medicine_x_weeks")
    object MedicationDetailFlow : Screen("medication_detail_flow/{medicationId}") {
        fun createRoute(medicationId: String) = "medication_detail_flow/$medicationId"
    }
    object SelectXDays : Screen("select_x_days")
    object SelectSpecificDays : Screen("select_specific_days")
    object SelectXWeeks : Screen("select_x_weeks")
}