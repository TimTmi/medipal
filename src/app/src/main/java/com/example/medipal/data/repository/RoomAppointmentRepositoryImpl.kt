package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.AppointmentDao
import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.domain.model.Appointment
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.util.ProfileRepositoryManager

class RoomAppointmentRepositoryImpl(
    private val dao: AppointmentDao,
    private val profileRepositoryManager: ProfileRepositoryManager
) : RoomRepositoryImpl<Appointment, AppointmentEntity>(
    { dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    { dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    { dao.getByIdAndProfileId(it, profileRepositoryManager.getCurrentProfileId()) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteByIdAndProfileId(it, profileRepositoryManager.getCurrentProfileId()) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), AppointmentRepository
