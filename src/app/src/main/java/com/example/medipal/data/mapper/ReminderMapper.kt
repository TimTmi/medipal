package com.example.medipal.data.mapper

import com.example.medipal.data.local.entity.ReminderEntity
import com.example.medipal.domain.model.Reminder

fun ReminderEntity.toDomain(): Reminder = Reminder(id, profileId, title, description, dateTime, isCompleted, updatedAt, deletedAt)
fun Reminder.toEntity(): ReminderEntity = ReminderEntity(id, profileId, title, description, dateTime, isCompleted, updatedAt, deletedAt)
