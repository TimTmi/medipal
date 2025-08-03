package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddMedicineViewModel(
    private val addMedicationUseCase: AddMedicationUseCase,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    // Các trạng thái cho từng bước
    val medicineName = MutableStateFlow("")
    val time = MutableStateFlow("16:46 AM") // Giả lập thời gian được chọn
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
    fun updateTime(newTime: String) {
        time.value = newTime
    }
    
    private val _lastSavedMedicineName = MutableStateFlow<String?>(null)
    val lastSavedMedicineName = _lastSavedMedicineName.asStateFlow()
    // Trạng thái cho dialog thành công
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()

    fun saveMedication() {
        viewModelScope.launch {
            val newMedication = ScheduledEvent.Medication(
                id = UUID.randomUUID().toString(),
                name = medicineName.value,
                dosage = "Frequency: ${selectedFrequency.value}", // Có thể thêm màn hình chọn liều lượng
                medicationTime = time.value
            )
            addMedicationUseCase(newMedication)
            
            // Auto-add to history
            historyRepository.addHistoryEntry(newMedication)
            
            _lastSavedMedicineName.value = newMedication.name
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedMedicineName.value = null
        _showSuccessDialog.value = false
    }
}