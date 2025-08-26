package com.example.medipal.domain.repository

import com.example.medipal.domain.model.CaregiverAssignment
import kotlinx.coroutines.flow.Flow

interface CaregiverAssignmentRepository {
    fun getAssignmentsForCaregiver(caregiverId: String): Flow<List<CaregiverAssignment>>
    fun getAssignmentsForCustomer(customerId: String): Flow<List<CaregiverAssignment>>

    suspend fun addAssignment(assignment: CaregiverAssignment)
    suspend fun removeAssignment(caregiverId: String, customerId: String)
}
