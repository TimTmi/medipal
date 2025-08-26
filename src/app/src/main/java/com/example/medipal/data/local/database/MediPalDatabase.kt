package com.example.medipal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medipal.data.local.dao.*
import com.example.medipal.data.local.entity.*
import androidx.room.TypeConverters

@Database(
    entities = [
        MedicationEntity::class,
        AppointmentEntity::class,
        ReminderEntity::class,
        ProfileEntity::class,
        MedicationDoseEntity::class,
        AppointmentStatusEntity::class,
        ReminderStatusEntity::class
    ],
    version = 19,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MediPalDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun reminderDao(): ReminderDao
    abstract fun profileDao(): ProfileDao
    abstract fun medicationDoseDao(): MedicationDoseDao
    abstract fun appointmentStatusDao(): AppointmentStatusDao
    abstract fun reminderStatusDao(): ReminderStatusDao
}
