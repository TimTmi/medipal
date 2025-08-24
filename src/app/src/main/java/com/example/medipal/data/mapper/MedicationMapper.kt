package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication

fun MedicationEntity.toDomain(): Medication = Medication(id, profileId, name, dosage, scheduleTime, description, frequency, updatedAt, deletedAt)
fun Medication.toEntity(): MedicationEntity = MedicationEntity(id, profileId, name, dosage, scheduleTime, description, frequency, updatedAt, deletedAt)
