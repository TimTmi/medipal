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
        ProfileEntity::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MediPalDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun reminderDao(): ReminderDao
    abstract fun profileDao(): ProfileDao
}
