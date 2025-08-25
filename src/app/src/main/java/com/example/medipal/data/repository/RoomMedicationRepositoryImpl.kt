package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDao
import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.util.ProfileRepositoryManager

class RoomMedicationRepositoryImpl(
    private val dao: MedicationDao,
    private val profileRepositoryManager: ProfileRepositoryManager
) : RoomRepositoryImpl<Medication, MedicationEntity>(
    { dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    { dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    { dao.getByIdAndProfileId(it, profileRepositoryManager.getCurrentProfileId()) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteByIdAndProfileId(it, profileRepositoryManager.getCurrentProfileId()) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), MedicationRepository