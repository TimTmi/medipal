package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.domain.model.Appointment

fun AppointmentEntity.toDomain(): Appointment = Appointment(id, profileId, title, description, dateTime, location, doctorName, updatedAt, deletedAt)
fun Appointment.toEntity(): AppointmentEntity = AppointmentEntity(id, profileId, title, description, dateTime, location, doctorName, updatedAt, deletedAt)
