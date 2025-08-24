package com.example.medipal.data.repository

import com.example.medipal.domain.model.Appointment
import com.example.medipal.data.local.dao.AppointmentDao
import com.example.medipal.data.local.dao.SyncDao
import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.AppointmentRepository

class RoomAppointmentRepositoryImpl(
    dao: AppointmentDao,
    syncDao: SyncDao
) : RoomRepositoryImpl<Appointment, AppointmentEntity>(
    { dao.getAll() },
    { dao.getAllOnce() },
    { dao.getById(it) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteById(it) },
    syncDao,
    "Appointment",
    { it.toDomain() },
    { it.toEntity() },
    { it.id },
    { it.updatedAt } // use a timestamp in Appointment
), AppointmentRepository
