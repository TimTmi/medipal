package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDao
import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.MedicationRepository

class RoomMedicationRepositoryImpl(
    private val dao: MedicationDao,
    private val profileId: String
) : RoomRepositoryImpl<Medication, MedicationEntity>(
    { dao.getAllByProfileId(profileId) },
    { dao.getAllOnceByProfileId(profileId) },
    { dao.getByIdAndProfileId(it, profileId) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteByIdAndProfileId(it, profileId) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), MedicationRepository