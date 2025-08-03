package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.ReminderRepository

// Use case để lấy medications
class GetMedicationsUseCase(private val repository: MedicationRepository) {
    operator fun invoke() = repository.getMedications()
}

// Use case để thêm một loại thuốc mới
class AddMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(medication: Medication) {
        repository.addMedication(medication)
    }
}

// Use case để cập nhật medication
class UpdateMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(medication: Medication) {
        repository.updateMedication(medication)
    }
}

// Use case để xóa medication
class RemoveMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(id: String) {
        repository.removeMedication(id)
    }
}

// Use case để lấy appointments
class GetAppointmentsUseCase(private val repository: AppointmentRepository) {
    operator fun invoke() = repository.getAppointments()
}

// Use case để thêm một cuộc hẹn mới
class AddAppointmentUseCase(private val repository: AppointmentRepository) {
    suspend operator fun invoke(appointment: Appointment) {
        repository.addAppointment(appointment)
    }
}

// Use case để lấy reminders
class GetRemindersUseCase(private val repository: ReminderRepository) {
    operator fun invoke() = repository.getReminders()
}

// Use case để thêm một healthcare reminder mới
class AddReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) {
        repository.addReminder(reminder)
    }
}