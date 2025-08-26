package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Frequency
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.service.NotificationService
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.*

class AddMedicationViewModel(
    private val addMedicationUseCase: AddMedicationUseCase,
    private val notificationService: NotificationService, // Assuming NotificationService is injected
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    // Sử dụng profile hiện tại động thay vì cố định

    val medicineName = MutableStateFlow("")
    val dosage = MutableStateFlow("")
    val description = MutableStateFlow("")
    val time = MutableStateFlow(System.currentTimeMillis()) // Thời gian được chọn dưới dạng Long
    val baseFrequencyOptions = listOf(
        "Every day",
        "Only as needed",
        "Every X days",
        "Specific days of the week",
        "Every X weeks"
    )

    // 2. State để lưu đối tượng Frequency cuối cùng. Khởi tạo giá trị mặc định.
    val selectedFrequencyObject = MutableStateFlow<Frequency>(Frequency.EveryDay())

    // 3. Các state cho các màn hình con (để người dùng nhập liệu)
    val xDaysValue = MutableStateFlow(2) // Giá trị mặc định cho "Every X days"
    val selectedWeekDays = MutableStateFlow<List<DayOfWeek>>(emptyList())
    val xWeeksValue = MutableStateFlow(1) // Giá trị mặc định cho "Every X weeks"

    fun setFrequencyEveryDay() {
        selectedFrequencyObject.value = Frequency.EveryDay()
    }
    fun setFrequencyAsNeeded() {
        selectedFrequencyObject.value = Frequency.AsNeeded()
    }
    fun saveFrequencyXDays(days: Int) {
        xDaysValue.value = days
        selectedFrequencyObject.value = Frequency.EveryXDays(days)
    }
    fun saveFrequencySpecificDays(days: List<DayOfWeek>) {
        selectedWeekDays.value = days
        selectedFrequencyObject.value = Frequency.SpecificDaysOfWeek(days)
    }
    fun saveFrequencyXWeeks(weeks: Int, days: List<DayOfWeek>) {
        xWeeksValue.value = weeks
        selectedWeekDays.value = days
        selectedFrequencyObject.value = Frequency.EveryXWeeks(weeks, days)
    }
    
    // Method to update selected time
    fun updateTime(newTime: Long) {
        time.value = newTime
    }

    private val _lastSavedMedicineName = MutableStateFlow<String?>(null)
    val lastSavedMedicineName = _lastSavedMedicineName.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()

    private val _medicineNameError = MutableStateFlow<String?>(null)
    val medicineNameError = _medicineNameError.asStateFlow()

    fun validateMedicineName(): Boolean {
        return if (medicineName.value.isBlank()) {
            _medicineNameError.value = "Medicine name is required"
            false
        } else {
            _medicineNameError.value = null
            true
        }
    }

    fun clearMedicineNameError() {
        _medicineNameError.value = null
    }

    fun saveMedication() {
        if (!validateMedicineName()) {
            return
        }
        
        viewModelScope.launch {
            val newMedication = Medication(
                id = UUID.randomUUID().toString(),
                name = medicineName.value,
                dosage = dosage.value.ifEmpty { "Standard dose" },
                scheduleTime = time.value,
                description = "Frequency: ${selectedFrequencyObject.value.displayText}",
                frequency = selectedFrequencyObject.value
            )
            addMedicationUseCase(newMedication, profileRepositoryManager.getCurrentProfileId())
            
            // Schedule notification
            notificationService.scheduleMedicationNotification(newMedication)
            
            _lastSavedMedicineName.value = newMedication.name
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedMedicineName.value = null
        _showSuccessDialog.value = false
    }
}