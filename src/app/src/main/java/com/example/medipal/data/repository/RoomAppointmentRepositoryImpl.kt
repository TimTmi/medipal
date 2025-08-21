package com.example.medipal.data.repository

import com.example.medipal.domain.model.Appointment
import com.example.medipal.data.local.dao.AppointmentDao
import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity

class RoomAppointmentRepositoryImpl(
    dao: AppointmentDao
) : RoomRepositoryImpl<Appointment, AppointmentEntity>(
    getAllFlow = { dao.getAll() },
    insert = { dao.insert(it) },
    update = { dao.update(it) },
    deleteById = { dao.deleteById(it) },
    toDomain = { it.toDomain() },
    toEntity = { it.toEntity() }
)
