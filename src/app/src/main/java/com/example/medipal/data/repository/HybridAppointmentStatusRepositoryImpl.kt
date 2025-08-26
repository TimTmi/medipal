package com.example.medipal.data.repository

import com.example.medipal.domain.model.AppointmentStatus
import com.example.medipal.domain.repository.AppointmentStatusRepository
import kotlinx.coroutines.flow.Flow

class HybridAppointmentStatusRepositoryImpl(
    private val roomRepository: RoomAppointmentStatusRepositoryImpl,
    private val firestoreRepository: FirestoreAppointmentStatusRepositoryImpl,
    private val isOnline: () -> Boolean
) : AppointmentStatusRepository {
    
    override fun getAll(): Flow<List<AppointmentStatus>> {
        return roomRepository.getAll()
    }
    
    override suspend fun getAllOnce(): List<AppointmentStatus> {
        return roomRepository.getAllOnce()
    }
    
    override suspend fun getById(id: String): AppointmentStatus? {
        return roomRepository.getById(id)
    }
    
    override suspend fun add(item: AppointmentStatus) {
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
    
    override suspend fun update(item: AppointmentStatus) {
        roomRepository.update(item)
        
        if (isOnline()) {
            try {
                firestoreRepository.update(item)
            } catch (e: Exception) {
                // Handle sync error silently
            }
        }
    }
    
    override suspend fun getByAppointmentAndTime(appointmentId: String, scheduledTime: Long): AppointmentStatus? {
        return roomRepository.getByAppointmentAndTime(appointmentId, scheduledTime)
    }
    
    override fun getByAppointmentId(appointmentId: String): Flow<List<AppointmentStatus>> {
        return roomRepository.getByAppointmentId(appointmentId)
    }
}
