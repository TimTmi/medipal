package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    getMedicationsUseCase: GetMedicationsUseCase,
    getAppointmentsUseCase: GetAppointmentsUseCase,
    getRemindersUseCase: GetRemindersUseCase
) : ViewModel() {

    // Giữ trạng thái của các danh sách riêng biệt
    val medications = getMedicationsUseCase()
    val appointments = getAppointmentsUseCase()
    val reminders = getRemindersUseCase()

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