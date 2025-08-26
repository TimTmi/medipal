package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.MedicationDoseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDoseDao {

    @Query("SELECT * FROM medication_dose WHERE profileId = :profileId")
    fun getAllByProfileId(profileId: String): Flow<List<MedicationDoseEntity>>

    @Query("SELECT * FROM medication_dose WHERE profileId = :profileId")
    suspend fun getAllOnceByProfileId(profileId: String): List<MedicationDoseEntity>

    @Query("SELECT * FROM medication_dose WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): MedicationDoseEntity?

    @Query("SELECT * FROM medication_dose WHERE medicationId = :medicationId AND profileId = :profileId")
    fun getByMedicationIdAndProfileId(medicationId: String, profileId: String): Flow<List<MedicationDoseEntity>>

    @Query("SELECT * FROM medication_dose WHERE medicationId = :medicationId AND scheduledTime = :scheduledTime AND profileId = :profileId LIMIT 1")
    suspend fun getByMedicationAndTimeAndProfileId(
        medicationId: String,
        scheduledTime: Long,
        profileId: String
    ): MedicationDoseEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MedicationDoseEntity)

    @Query("DELETE FROM medication_dose WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: MedicationDoseEntity)

    // Legacy methods for backward compatibility
    @Query("SELECT * FROM medication_dose")
    fun getAll(): Flow<List<MedicationDoseEntity>>

    @Query("SELECT * FROM medication_dose")
    suspend fun getAllOnce(): List<MedicationDoseEntity>
}
