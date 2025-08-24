package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Medication // Import model của bạn
import com.example.medipal.domain.usecase.GetMedicationByIdUseCase
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.UpdateMedicationUseCase
import com.example.medipal.domain.usecase.RemoveMedicationUseCase
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.medipal.domain.model.Frequency
import android.util.Log
import java.time.DayOfWeek

class MedicationDetailViewModel(
    savedStateHandle: SavedStateHandle,
    // Các instance của UseCase được cung cấp sẵn ở đây
    private val getMedicationByIdUseCase: GetMedicationByIdUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val deleteMedicationUseCase: RemoveMedicationUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    private val medicationId: String = checkNotNull(savedStateHandle["medicationId"])
    private val profileId = profileRepositoryManager.getCurrentProfileId()

    private val _medication = MutableStateFlow<Medication?>(null)
    val medication = _medication.asStateFlow()

    val medicineName = MutableStateFlow("")
    val dosage = MutableStateFlow("")
    val description = MutableStateFlow("")

    // Frequency options và selected frequency object
    val baseFrequencyOptions = listOf(
        "Every day",
        "Only as needed",
        "Every X days",
        "Specific days of the week",
        "Every X weeks"
    )
    val selectedFrequencyObject = MutableStateFlow<Frequency>(Frequency.EveryDay())
    
    // Các state cho các màn hình con (để người dùng nhập liệu)
    val xDaysValue = MutableStateFlow(2) // Giá trị mặc định cho "Every X days"
    val selectedWeekDays = MutableStateFlow<List<DayOfWeek>>(emptyList())
    val xWeeksValue = MutableStateFlow(1) // Giá trị mặc định cho "Every X weeks"

    init {
        Log.d("ViewModelLifecycle", "MedicationDetailViewModel created for medicationId: $medicationId. Instance: ${this.hashCode()}")
        viewModelScope.launch {
            val med = getMedicationByIdUseCase(medicationId, profileId)
            _medication.value = med

            med?.let { medicationData ->
                medicineName.value = medicationData.name
                dosage.value = medicationData.dosage
                description.value = medicationData.description ?: ""
                selectedFrequencyObject.value = medicationData.frequency

                // Cập nhật các state con dựa trên frequency hiện tại
                when (medicationData.frequency) {
                    is Frequency.EveryXDays -> {
                        xDaysValue.value = medicationData.frequency.days
                    }
                    is Frequency.SpecificDaysOfWeek -> {
                        selectedWeekDays.value = medicationData.frequency.days
                    }
                    is Frequency.EveryXWeeks -> {
                        xWeeksValue.value = medicationData.frequency.weeks
                        selectedWeekDays.value = medicationData.frequency.days
                    }
                    else -> {
                        // Không cần cập nhật gì cho EveryDay và AsNeeded
                    }
                }
            }
        }
    }

    // Các hàm để cập nhật frequency
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

    fun onUpdate(onSuccess: () -> Unit) {
        viewModelScope.launch {
            Log.d("ViewModelLifecycle", "MedicationDetailViewModel created for medicationId: $medicationId. Instance: ${this.hashCode()}")
            val currentMed = _medication.value ?: return@launch
            val updatedMed = currentMed.copy(
                name = medicineName.value,
                dosage = dosage.value,
                description = description.value,
                frequency = selectedFrequencyObject.value
            )
            updateMedicationUseCase(updatedMed)
            onSuccess()
        }
    }

    fun onDelete(onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteMedicationUseCase(medicationId)
            onSuccess()
        }
    }
}