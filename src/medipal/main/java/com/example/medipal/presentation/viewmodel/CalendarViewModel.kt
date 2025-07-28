package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// Data class để giữ trạng thái của lịch
data class CalendarUiState(
    // Ngày đang được chọn, mặc định là hôm nay
    val selectedDate: LocalDate = LocalDate.now(),
    // Danh sách các ngày để hiển thị trong lưới 2 tuần
    val dates: List<Date> = emptyList()
) {
    // Lớp con để biểu diễn một ngày trong lịch
    data class Date(
        val date: LocalDate,
        val isCurrentMonth: Boolean, // Vẫn giữ để làm mờ ngày tháng khác
        val isSelected: Boolean      // Thay thế isToday bằng isSelected
    )

    // Tiêu đề tháng của ngày đang được chọn
    val monthTitle: String
        get() = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(Locale.getDefault()))
}


class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Khởi tạo lịch xoay quanh ngày hôm nay
        generateTwoWeekGrid(LocalDate.now())
    }

    // Hàm mới để tạo lưới 2 tuần xoay quanh một ngày trung tâm
    private fun generateTwoWeekGrid(centerDate: LocalDate) {
        // Tìm ngày thứ Hai của tuần chứa ngày trung tâm
        val startOfWeek = centerDate.with(DayOfWeek.MONDAY)

        // Tạo ra một danh sách 14 ngày (2 tuần)
        val dates = (0 until 14).map {
            val date = startOfWeek.plusDays(it.toLong())
            CalendarUiState.Date(
                date = date,
                isCurrentMonth = date.monthValue == centerDate.monthValue,
                isSelected = date.isEqual(centerDate) // Kiểm tra xem có phải là ngày được chọn không
            )
        }

        _uiState.update { it.copy(selectedDate = centerDate, dates = dates) }
    }

    // Hàm này được gọi từ UI khi người dùng nhấn vào một ngày
    fun onDateSelected(date: LocalDate) {
        generateTwoWeekGrid(date)
    }

    // Các mũi tên bây giờ sẽ chuyển tuần thay vì chuyển tháng
    fun goToNextWeek() {
        val nextWeekDate = _uiState.value.selectedDate.plusWeeks(1)
        generateTwoWeekGrid(nextWeekDate)
    }

    fun goToPreviousWeek() {
        val previousWeekDate = _uiState.value.selectedDate.minusWeeks(1)
        generateTwoWeekGrid(previousWeekDate)
    }
}