package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.usecase.AddMedicationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddMedicineViewModel(private val addMedicationUseCase: AddMedicationUseCase) : ViewModel() {

    // Các trạng thái cho từng bước
    val medicineName = MutableStateFlow("Paracetamol")
    val frequency = MutableStateFlow("")
    val time = MutableStateFlow("16:46 AM") // Giả lập thời gian được chọn

    // Trạng thái cho dialog thành công
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()

    fun saveMedication() {
        viewModelScope.launch {
            val newMedication = ScheduledEvent.Medication(
                id = UUID.randomUUID().toString(),
                name = medicineName.value,
                dosage = "Dosage details here", // Có thể thêm màn hình chọn liều lượng
                medicationTime = time.value
            )
            addMedicationUseCase(newMedication)
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
    }
}