package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope

class HybridMedicationRepositoryImpl(
    localRepo: LocalRepository<Medication>,
    remoteRepo: RemoteRepository<Medication>,
    networkChecker: () -> Boolean
) : HybridRepositoryImpl<Medication>(
    localRepo = localRepo,
    remoteRepo = remoteRepo,
    networkChecker = networkChecker,
    getId = { it.id },
    getUpdatedAt = { it.updatedAt },
    getDeletedAt = { it.deletedAt },
    copyWithUpdated = { item, time -> item.copy(updatedAt = time) },
    copyWithDeleted = { item, time -> item.copy(deletedAt = time) }
), MedicationRepository