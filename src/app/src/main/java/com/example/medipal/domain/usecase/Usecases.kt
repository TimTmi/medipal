package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.MedicationRepository

// Use case để lấy các sự kiện cho màn hình chính
class GetScheduledEventsUseCase(private val repository: MedicationRepository) {
    operator fun invoke() = repository.getScheduledEvents()
}

// Use case để thêm một loại thuốc mới
class AddMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(medication: ScheduledEvent.Medication) {
        repository.addMedication(medication)
    }
}
class AddHealthcareReminderUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(reminder: ScheduledEvent.Reminder) {
        repository.addHealthcareReminder(reminder)
    }

}