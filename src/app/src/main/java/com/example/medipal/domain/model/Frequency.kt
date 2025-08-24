package com.example.medipal.domain.model
import java.time.DayOfWeek

sealed class Frequency(val displayText: String) {
    object EveryDay : Frequency("Every day")
    object AsNeeded : Frequency("Only as needed")

    // Các lớp con chứa dữ liệu cụ thể
    data class EveryXDays(val days: Int) : Frequency("Every $days days")
    data class SpecificDaysOfWeek(val days: Set<DayOfWeek>) : Frequency(
        when {
            days.isEmpty() -> "Specific days"
            days.size == 1 -> days.first().name.lowercase().replaceFirstChar { it.uppercase() }
            days.size == 7 -> "Every day"
            else -> {
                val dayNames = days.sortedBy { it.value }.map { 
                    it.name.lowercase().replaceFirstChar { it.uppercase() }
                }
                dayNames.joinToString(", ")
            }
        }
    )

    data class EveryXWeeks(val weeks: Int, val days: Set<DayOfWeek>) : Frequency(
        when {
            days.isEmpty() -> "Every $weeks weeks"
            days.size == 1 -> "Every $weeks weeks on ${days.first().name.lowercase().replaceFirstChar { it.uppercase() }}"
            else -> {
                val dayNames = days.sortedBy { it.value }.map { 
                    it.name.lowercase().replaceFirstChar { it.uppercase() }
                }
                "Every $weeks weeks on ${dayNames.joinToString(", ")}"
            }
        }
    )

    companion object {
        // Chuyển đổi từ chuỗi sang đối tượng
        // Logic này sẽ cần phức tạp hơn, tạm thời để đơn giản
        fun fromDisplayText(text: String): Frequency {
            return when {
                text == "Every day" -> EveryDay
                text == "Only as needed" -> AsNeeded
                // ... cần logic phân tích chuỗi phức tạp hơn
                else -> EveryDay // Mặc định
            }
        }
    }
}
