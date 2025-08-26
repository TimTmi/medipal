package com.example.medipal.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.util.NetworkObserver
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.koin.dsl.module

class HomeViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    // Observe profile changes and update data accordingly
    val medications = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getMedicationsUseCase(profileId)
    }
    
    val appointments = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getAppointmentsUseCase(profileId)
    }
    
    val reminders = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getRemindersUseCase(profileId)
    }

    // Giữ trạng thái hiển thị của BottomSheet
    private val _isAddSheetVisible = MutableStateFlow(false)
    val isAddSheetVisible = _isAddSheetVisible.asStateFlow()

    fun showAddSheet() {
        _isAddSheetVisible.value = true
    }

    fun hideAddSheet() {
        _isAddSheetVisible.value = false
    }
}