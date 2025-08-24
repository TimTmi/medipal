package com.example.medipal.data.repository

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope

class HybridAppointmentRepositoryImpl(
    localRepo: LocalRepository<Appointment>,
    remoteRepo: RemoteRepository<Appointment>,
    networkChecker: () -> Boolean,
    appScope: CoroutineScope
) : HybridRepositoryImpl<Appointment>(
    localRepo = localRepo,
    remoteRepo = remoteRepo,
    networkChecker = networkChecker,
    appScope = appScope,
    getId = { it.id },
    getUpdatedAt = { it.updatedAt },
    getDeletedAt = { it.deletedAt },
    copyWithUpdated = { item, time -> item.copy(updatedAt = time) },
    copyWithDeleted = { item, time -> item.copy(deletedAt = time) }
), AppointmentRepository