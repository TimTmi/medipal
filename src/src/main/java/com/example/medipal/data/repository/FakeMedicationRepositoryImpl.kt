package com.example.medipal.data.repository


import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

// Triển khai "fake" repository để giả lập dữ liệu
class FakeMedicationRepositoryImpl : MedicationRepository {

    // Dùng MutableStateFlow để có thể thêm và cập nhật dữ liệu một cách linh hoạt
    private val eventsFlow = MutableStateFlow<List<ScheduledEvent>>(
        listOf(
            ScheduledEvent.Medication("med1", "Paracetamol (500 mg)", "500mg, take 1 tablet(s)", "7:00 AM"),
            ScheduledEvent.Appointment("apt1", "Health Check-up", "7:00 AM", "At: Bach Mai Quan 1"),
            ScheduledEvent.Reminder("rem1", "Go out for a walk", "7:00 AM")
        )
    )

    override fun getScheduledEvents(): Flow<List<ScheduledEvent>> {
        return eventsFlow
    }

    override suspend fun addMedication(medication: ScheduledEvent.Medication) {
        val currentList = eventsFlow.value.toMutableList()
        currentList.add(0, medication) // Thêm vào đầu danh sách
        eventsFlow.value = currentList
    }
}