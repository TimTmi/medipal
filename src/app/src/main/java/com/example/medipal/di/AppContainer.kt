package com.example.medipal.di

import android.content.Context
import androidx.room.Room
import com.example.medipal.data.local.database.MediPalDatabase
import com.example.medipal.data.repository.RoomAppointmentRepositoryImpl
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
import com.example.medipal.data.repository.RoomReminderRepositoryImpl
//import com.example.medipal.data.repository.HistoryRepositoryImpl
import com.example.medipal.domain.repository.AppointmentRepository

//import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.usecase.AddReminderUseCase
import com.example.medipal.domain.usecase.AddAppointmentUseCase
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetMedicationByIdUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.UpdateMedicationUseCase
import com.example.medipal.domain.usecase.RemoveMedicationUseCase

/**
 * Dependency injection container at the application level.
 */
interface AppContainer {
    val medicationRepository: MedicationRepository
    val appointmentRepository: AppointmentRepository
    val reminderRepository: ReminderRepository
//    val historyRepository: HistoryRepository
    val getMedicationsUseCase: GetMedicationsUseCase
    val getAppointmentsUseCase: GetAppointmentsUseCase
    val getRemindersUseCase: GetRemindersUseCase
    val addMedicationUseCase: AddMedicationUseCase
    val addReminderUseCase: AddReminderUseCase
    val addAppointmentUseCase: AddAppointmentUseCase
    val updateMedicationUseCase: UpdateMedicationUseCase
    val removeMedicationUseCase: RemoveMedicationUseCase
    val getMedicationByIdUseCase: GetMedicationByIdUseCase
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val database: MediPalDatabase = Room.databaseBuilder(
        context.applicationContext,
        MediPalDatabase::class.java,
        "medipal.db"
    )
    .addMigrations(MIGRATION_2_3) // Thêm migration từ version 2 sang 3
    .fallbackToDestructiveMigration() // Fallback nếu migration thất bại
    .build()
    
    // Repository
    override val medicationRepository: MedicationRepository by lazy {
        RoomMedicationRepositoryImpl(database.medicationDao())
    }

    override val appointmentRepository: AppointmentRepository by lazy {
        RoomAppointmentRepositoryImpl(database.appointmentDao())
    }

    override val reminderRepository: ReminderRepository by lazy {
        RoomReminderRepositoryImpl(database.reminderDao())
    }
    
//    override val historyRepository: HistoryRepository by lazy {
//        HistoryRepositoryImpl()
//    }

    // Use Cases
    override val getMedicationsUseCase: GetMedicationsUseCase by lazy {
        GetMedicationsUseCase(medicationRepository)
    }
    override val getMedicationByIdUseCase: GetMedicationByIdUseCase by lazy {
        GetMedicationByIdUseCase(medicationRepository)
    }

    override val getAppointmentsUseCase: GetAppointmentsUseCase by lazy {
        GetAppointmentsUseCase(appointmentRepository)
    }

    override val getRemindersUseCase: GetRemindersUseCase by lazy {
        GetRemindersUseCase(reminderRepository)
    }
    
    override val addMedicationUseCase: AddMedicationUseCase by lazy {
        AddMedicationUseCase(medicationRepository)
    }

    override val addReminderUseCase: AddReminderUseCase by lazy {
        AddReminderUseCase(reminderRepository)
    }

    override val addAppointmentUseCase: AddAppointmentUseCase by lazy {
        AddAppointmentUseCase(appointmentRepository)
    }

    override val updateMedicationUseCase: UpdateMedicationUseCase by lazy {
        UpdateMedicationUseCase(medicationRepository)
    }

    override val removeMedicationUseCase: RemoveMedicationUseCase by lazy {
        RemoveMedicationUseCase(medicationRepository)
    }
    
    companion object {
        /**
         * Migration từ version 2 sang 3 để handle việc chuyển đổi từ enum Frequency sang sealed class
         * 
         * Cách tiếp cận: Chuyển đổi tất cả frequency cũ thành "EVERY_DAY" vì:
         * 1. Đây là giá trị an toàn nhất
         * 2. Người dùng có thể chỉnh sửa lại frequency sau khi update app
         * 3. Tránh mất dữ liệu
         */
        private val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Cập nhật tất cả frequency trong bảng medication thành "EVERY_DAY"
                database.execSQL("UPDATE medication SET frequency = 'EVERY_DAY'")
                
                // Cập nhật tất cả frequency trong bảng reminder thành "EVERY_DAY"
                database.execSQL("UPDATE reminder SET frequency = 'EVERY_DAY'")
            }
        }
    }
}
