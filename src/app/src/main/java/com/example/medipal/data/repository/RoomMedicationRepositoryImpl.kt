package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDao
import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity

class RoomMedicationRepositoryImpl(
    dao: MedicationDao
) : RoomRepositoryImpl<Medication, MedicationEntity>(
    getAllFlow = { dao.getAll() },
    insert = { dao.insert(it) },
    update = { dao.update(it) },
    deleteById = { dao.deleteById(it) },
    toDomain = { it.toDomain() },
    toEntity = { it.toEntity() }
)