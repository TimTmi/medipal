package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication

fun MedicationEntity.toDomain(): Medication = Medication(id, name, dosage, scheduleTime, notes)
fun Medication.toEntity(): MedicationEntity = MedicationEntity(id, name, dosage, scheduleTime, notes)
