package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.domain.model.Appointment

fun AppointmentEntity.toDomain(): Appointment = Appointment(id, title, scheduleTime, doctor, notes)
fun Appointment.toEntity(): AppointmentEntity = AppointmentEntity(id, title, scheduleTime, doctor, notes)
