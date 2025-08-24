package com.example.medipal.data.repository

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope

class HybridReminderRepositoryImpl(
    localRepo: LocalRepository<Reminder>,
    remoteRepo: RemoteRepository<Reminder>,
    networkChecker: () -> Boolean,
    appScope: CoroutineScope
) : HybridRepositoryImpl<Reminder>(
    localRepo = localRepo,
    remoteRepo = remoteRepo,
    networkChecker = networkChecker,
    appScope = appScope,
    getId = { it.id },
    getUpdatedAt = { it.updatedAt },
    getDeletedAt = { it.deletedAt },
    copyWithUpdated = { item, time -> item.copy(updatedAt = time) },
    copyWithDeleted = { item, time -> item.copy(deletedAt = time) }
), ReminderRepository