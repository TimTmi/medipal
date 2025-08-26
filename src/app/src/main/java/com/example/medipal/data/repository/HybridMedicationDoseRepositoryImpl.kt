package com.example.medipal.data.repository

import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import kotlinx.coroutines.flow.Flow

class HybridMedicationDoseRepositoryImpl(
    private val roomRepository: RoomMedicationDoseRepositoryImpl,
    private val firestoreRepository: FirestoreMedicationDoseRepositoryImpl,
    private val isOnline: () -> Boolean
) : MedicationDoseRepository {
    
    override fun getAll(): Flow<List<MedicationDose>> {
        return roomRepository.getAll()
    }
    
    override suspend fun getAllOnce(): List<MedicationDose> {
        return roomRepository.getAllOnce()
    }
    
    override suspend fun getById(id: String): MedicationDose? {
        return roomRepository.getById(id)
    }
    
    override suspend fun add(item: MedicationDose) {
        // Always save to Room first
        roomRepository.add(item)
        
        // Sync to Firestore if online
        if (isOnline()) {
            try {
                firestoreRepository.add(item)
            } catch (e: Exception) {
                // Handle sync error silently
            }
        }
    }
    
    override suspend fun remove(id: String) {
        roomRepository.remove(id)
        
        if (isOnline()) {
            try {
                firestoreRepository.remove(id)
            } catch (e: Exception) {
                // Handle sync error silently
            }
        }
    }
    
    override suspend fun update(item: MedicationDose) {
        roomRepository.update(item)
        
        if (isOnline()) {
            try {
                firestoreRepository.update(item)
            } catch (e: Exception) {
                // Handle sync error silently
            }
        }
    }
    
    override fun getByMedicationId(medicationId: String): Flow<List<MedicationDose>> {
        return roomRepository.getByMedicationId(medicationId)
    }
    
    override suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose? {
        return roomRepository.getByMedicationAndTime(medicationId, scheduledTime)
    }
}
