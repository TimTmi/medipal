package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ProfileDao
import com.example.medipal.data.local.entity.ProfileEntity
import com.example.medipal.domain.model.Profile
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.ProfileRepository

class RoomProfileRepositoryImpl(
    dao: ProfileDao
) : RoomRepositoryImpl<Profile, ProfileEntity>(
    { dao.getAll() },
    { dao.getAllOnce() },
    { dao.getById(it) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteById(it) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), ProfileRepository