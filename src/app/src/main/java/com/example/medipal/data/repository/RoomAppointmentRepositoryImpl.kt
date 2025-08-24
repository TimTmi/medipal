package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.AppointmentDao
import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.domain.model.Appointment
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.AppointmentRepository

class RoomAppointmentRepositoryImpl(
    private val dao: AppointmentDao,
    private val profileId: String
) : RoomRepositoryImpl<Appointment, AppointmentEntity>(
    { dao.getAllByProfileId(profileId) },
    { dao.getAllOnceByProfileId(profileId) },
    { dao.getByIdAndProfileId(it, profileId) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteByIdAndProfileId(it, profileId) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), AppointmentRepository
