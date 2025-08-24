package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.domain.model.Medication

fun MedicationEntity.toDomain(): Medication = Medication(this.id, this.name, this.dosage, this.scheduleTime, this.notes,this.frequency)
fun Medication.toEntity(): MedicationEntity = MedicationEntity(this.id, this.name, this.dosage, this.scheduleTime, this.notes, this.frequency)
