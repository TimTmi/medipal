package com.example.medipal.data.repository

import com.example.medipal.domain.model.ReminderStatus
import com.example.medipal.domain.repository.ReminderStatusRepository
import kotlinx.coroutines.flow.Flow

class HybridReminderStatusRepositoryImpl(
    private val roomRepository: RoomReminderStatusRepositoryImpl,
    private val firestoreRepository: FirestoreReminderStatusRepositoryImpl,
    private val isOnline: () -> Boolean
) : ReminderStatusRepository {
    
    override fun getAll(): Flow<List<ReminderStatus>> {
        return roomRepository.getAll()
    }
    
    override suspend fun getAllOnce(): List<ReminderStatus> {
        return roomRepository.getAllOnce()
    }
    
    override suspend fun getById(id: String): ReminderStatus? {
        return roomRepository.getById(id)
    }
    
    override suspend fun add(item: ReminderStatus) {
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
    
    override suspend fun update(item: ReminderStatus) {
        roomRepository.update(item)
        
        if (isOnline()) {
            try {
                firestoreRepository.update(item)
            } catch (e: Exception) {
                // Handle sync error silently
            }
        }
    }
    
    override suspend fun getByReminderAndTime(reminderId: String, scheduledTime: Long): ReminderStatus? {
        return roomRepository.getByReminderAndTime(reminderId, scheduledTime)
    }
    
    override fun getByReminderId(reminderId: String): Flow<List<ReminderStatus>> {
        return roomRepository.getByReminderId(reminderId)
    }
}
