package com.example.medipal.data.repository

import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.ProfileRepository
import com.example.medipal.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope

class HybridProfileRepositoryImpl(
    localRepo: LocalRepository<Profile>,
    remoteRepo: RemoteRepository<Profile>,
    networkChecker: () -> Boolean,
    appScope: CoroutineScope
) : HybridRepositoryImpl<Profile>(
    localRepo = localRepo,
    remoteRepo = remoteRepo,
    networkChecker = networkChecker,
    appScope = appScope,
    getId = { it.id },
    getUpdatedAt = { it.updatedAt },
    getDeletedAt = { it.deletedAt },
    copyWithUpdated = { item, time -> item.copy(updatedAt = time) },
    copyWithDeleted = { item, time -> item.copy(deletedAt = time) }
), ProfileRepository