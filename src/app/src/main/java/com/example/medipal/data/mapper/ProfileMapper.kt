package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.ProfileEntity
import com.example.medipal.domain.model.Profile

fun ProfileEntity.toDomain(): Profile =
    Profile(id, fullName, birthday, height, weight, conditions, avatarUrl, updatedAt, deletedAt)

fun Profile.toEntity(): ProfileEntity =
    ProfileEntity(id, fullName, birthday, height, weight, conditions, avatarUrl, updatedAt, deletedAt)