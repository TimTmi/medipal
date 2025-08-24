package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Medication

import com.example.medipal.domain.usecase.AddMedicationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddMedicationViewModel(
    private val addMedicationUseCase: AddMedicationUseCase
) : ViewModel() {

    val medicineName = MutableStateFlow("")
    val time = MutableStateFlow(System.currentTimeMillis()) // Thời gian được chọn dưới dạng Long
    val frequencyOptions = listOf(
        "Every day",
        "Only as needed",
        "Every X days",
        "Specific days of the week",
        "Every X weeks"

    )

    val selectedFrequency = MutableStateFlow(frequencyOptions.first())

    // SỬA LỖI 2: Đảm bảo cú pháp hàm đúng
    fun onFrequencySelected(frequency: String) {
        selectedFrequency.value = frequency
    }
    
    // Method to update selected time
    fun updateTime(newTime: Long) {
        time.value = newTime
    }

    private val _lastSavedMedicineName = MutableStateFlow<String?>(null)
    val lastSavedMedicineName = _lastSavedMedicineName.asStateFlow()
    // Trạng thái cho dialog thành công
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()

    fun saveMedication() {
        viewModelScope.launch {
            val newMedication = Medication(
                id = UUID.randomUUID().toString(),
                name = medicineName.value,
                dosage = "Frequency: ${selectedFrequency.value}", // Có thể thêm màn hình chọn liều lượng
                scheduleTime = time.value,
            )
            addMedicationUseCase(newMedication)

            _lastSavedMedicineName.value = newMedication.name
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedMedicineName.value = null
        _showSuccessDialog.value = false
    }
}