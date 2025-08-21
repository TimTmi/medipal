package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
import com.example.medipal.data.repository.FirestoreMedicationRepositoryImpl
import com.example.medipal.util.NetworkChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HybridMedicationRepositoryImpl() {}

//class HybridMedicationRepositoryImpl(
//    private val localRepo: RoomMedicationRepositoryImpl,
//    private val remoteRepo: FirestoreMedicationRepositoryImpl,
//    private val networkChecker: NetworkChecker,
//    private val appScope: CoroutineScope
//) : Repository<Medication> {
//
//    override fun getMedications(): Flow<List<Medication>> =
//        localRepo.getMedications()
//            .also {
//                appScope.launch(Dispatchers.IO) {
//                    if (networkChecker.isOnline()) {
//                        syncWithRemote()
//                    }
//                }
//            }
//
//    override suspend fun addMedication(med: Medication) {
//        val updated = med.copy(lastModified = System.currentTimeMillis())
//        localRepo.addMedication(updated)
//        if (networkChecker.isOnline()) {
//            remoteRepo.addMedication(updated)
//        } else {
//            localRepo.markNeedsSync(updated.id, true)
//        }
//    }
//
//    override suspend fun updateMedication(med: Medication) {
//        val updated = med.copy(lastModified = System.currentTimeMillis())
//        localRepo.updateMedication(updated)
//        if (networkChecker.isOnline()) {
//            remoteRepo.updateMedication(updated)
//        } else {
//            localRepo.markNeedsSync(updated.id, true)
//        }
//    }
//
//    override suspend fun removeMedication(id: String) {
//        val deleted = localRepo.getById(id)?.copy(
//            isDeleted = true,
//            lastModified = System.currentTimeMillis()
//        )
//        if (deleted != null) {
//            localRepo.updateMedication(deleted)
//            if (networkChecker.isOnline()) {
//                remoteRepo.updateMedication(deleted)
//            } else {
//                localRepo.markNeedsSync(id, true)
//            }
//        }
//    }
//
//    /**
//     * Merge logic: compare remote vs local and update accordingly.
//     */
//    private suspend fun syncWithRemote() {
//        val localMeds = localRepo.getAllOnce()
//        val remoteMeds = remoteRepo.getMedications().firstOrNull() ?: emptyList()
//
//        val merged = mutableListOf<Medication>()
//
//        val allIds = (localMeds.map { it.id } + remoteMeds.map { it.id }).toSet()
//        for (id in allIds) {
//            val local = localMeds.find { it.id == id }
//            val remote = remoteMeds.find { it.id == id }
//
//            val winner = when {
//                local == null && remote != null -> remote
//                remote == null && local != null -> local
//                local != null && remote != null -> {
//                    if (local.lastModified >= remote.lastModified) local else remote
//                }
//                else -> null
//            }
//
//            if (winner != null) merged += winner
//        }
//
//        // Update local DB to match merged state
//        localRepo.replaceAll(merged)
//
//        // Push unsynced local changes back to remote
//        val unsynced = localMeds.filter { it.needsSync }
//        unsynced.forEach { med ->
//            if (med.isDeleted) remoteRepo.removeMedication(med.id)
//            else remoteRepo.updateMedication(med)
//            localRepo.markNeedsSync(med.id, false)
//        }
//    }
//}
