package com.example.medipal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medipal.data.local.dao.MedicationDao
import com.example.medipal.data.local.dao.AppointmentDao
import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.data.local.entity.MedicationEntity
import com.example.medipal.data.local.entity.AppointmentEntity
import com.example.medipal.data.local.entity.ReminderEntity
import androidx.room.TypeConverters
@Database(
    entities = [
        MedicationEntity::class,
        AppointmentEntity::class,
        ReminderEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MediPalDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun reminderDao(): ReminderDao
}
