package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.UpdateMedicationUseCase
import com.example.medipal.domain.usecase.RemoveMedicationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MedicationListViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val removeMedicationUseCase: RemoveMedicationUseCase
) : ViewModel() {

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // All medications from repository
    private val allMedications = getMedicationsUseCase()

    // Filtered medications based on search query
    val filteredMedications = combine(
        allMedications,
        _searchQuery
    ) { medications, query ->
        if (query.isBlank()) {
            medications
        } else {
            medications.filter { medication ->
                medication.name.contains(query, ignoreCase = true)
            }
        }
    }

    // Selected medication for editing
    private val _selectedMedication = MutableStateFlow<Medication?>(null)
    val selectedMedication: StateFlow<Medication?> = _selectedMedication.asStateFlow()

    // Edit dialog state
    private val _isEditDialogVisible = MutableStateFlow(false)
    val isEditDialogVisible: StateFlow<Boolean> = _isEditDialogVisible.asStateFlow()

    // Search dialog state
    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible: StateFlow<Boolean> = _isSearchVisible.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun showSearch() {
        _isSearchVisible.value = true
    }

    fun hideSearch() {
        _isSearchVisible.value = false
        _searchQuery.value = ""
    }

    fun selectMedicationForEdit(medication: Medication) {
        _selectedMedication.value = medication
        _isEditDialogVisible.value = true
    }

    fun hideEditDialog() {
        _isEditDialogVisible.value = false
        _selectedMedication.value = null
    }

    fun updateMedication(medication: Medication) {
        viewModelScope.launch {
            try {
                updateMedicationUseCase(medication)
                hideEditDialog()
            } catch (e: Exception) {
                // Handle error - could add error state here
            }
        }
    }

    fun deleteMedication(medicationId: String) {
        viewModelScope.launch {
            try {
                removeMedicationUseCase(medicationId)
                hideEditDialog()
            } catch (e: Exception) {
                // Handle error - could add error state here
            }
        }
    }

    fun getMedicationStatus(scheduleTime: Long): MedicationStatus {
        val currentTime = System.currentTimeMillis()
        val timeDiff = scheduleTime - currentTime
        
        return when {
            timeDiff > 3600000 -> MedicationStatus.UPCOMING // More than 1 hour
            timeDiff > 0 -> MedicationStatus.UPCOMING // Less than 1 hour but still upcoming
            timeDiff > -3600000 -> MedicationStatus.SKIPPED // Less than 1 hour late
            else -> MedicationStatus.TAKEN // Assume taken if very old
        }
    }
}

enum class MedicationStatus {
    TAKEN,
    SKIPPED,
    UPCOMING
}
