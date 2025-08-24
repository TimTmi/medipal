package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.model.ScheduledItem
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

//class GetScheduledEventsUseCase(
//    private val medicationRepo: MedicationRepository,
//    private val appointmentRepo: AppointmentRepository,
//    private val reminderRepo: ReminderRepository
//) {
//    operator fun invoke(): Flow<List<ScheduledItem>> {
//        return combine(
//            medicationRepo.getAll(),
//            appointmentRepo.getAll(),
//            reminderRepo.getAll()
//        ) { medications, appointments, reminders ->
//            val medItems = medications.map { ScheduledItem.MedicationItem(it) }
//            val aptItems = appointments.map { ScheduledItem.AppointmentItem(it) }
//            val remItems = reminders.map { ScheduledItem.ReminderItem(it) }
//
//            (medItems + aptItems + remItems)
//                .sortedBy { it.dateTime } // Optional: sort by time
//        }
//    }
//}
