package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.medipal.domain.usecase.GetScheduledEventsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(getScheduledEventsUseCase: GetScheduledEventsUseCase) : ViewModel() {

    // Giữ trạng thái của danh sách sự kiện
    val events = getScheduledEventsUseCase()

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