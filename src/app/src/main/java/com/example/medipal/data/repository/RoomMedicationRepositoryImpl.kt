package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDao
import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.MedicationRepository

class RoomMedicationRepositoryImpl(
    dao: MedicationDao
) : RoomRepositoryImpl<Medication, MedicationEntity>(
    { dao.getAll() },
    { dao.getAllOnce() },
    { dao.getById(it) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteById(it) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), MedicationRepository