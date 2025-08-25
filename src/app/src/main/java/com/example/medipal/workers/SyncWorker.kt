package com.example.medipal.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medipal.data.repository.HybridAppointmentRepositoryImpl
import com.example.medipal.data.repository.HybridMedicationRepositoryImpl
import com.example.medipal.data.repository.HybridReminderRepositoryImpl
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.get

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    val medicationRepo: MedicationRepository = get<MedicationRepository>(clazz = MedicationRepository::class.java)
    val appointmentRepo: AppointmentRepository = get<AppointmentRepository>(clazz = AppointmentRepository::class.java)
    val reminderRepo: ReminderRepository = get<ReminderRepository>(clazz = ReminderRepository::class.java)

    override suspend fun doWork(): Result {
        if (medicationRepo is HybridMedicationRepositoryImpl) {
            medicationRepo.syncAll()
        }

        if (appointmentRepo is HybridAppointmentRepositoryImpl) {
            appointmentRepo.syncAll()
        }

        if (reminderRepo is HybridReminderRepositoryImpl) {
            reminderRepo.syncAll()
        }
        return Result.success()
    }
}
