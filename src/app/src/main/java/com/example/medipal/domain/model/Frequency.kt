package com.example.medipal.domain.model
import java.time.DayOfWeek

open class Frequency(val displayText: String = "") {
    // No-argument constructor for Firestore
    constructor() : this("")
    
    // Override toString to return displayText for proper display
    override fun toString(): String = displayText
    
    class EveryDay() : Frequency("Every day")
    
    class AsNeeded() : Frequency("Only as needed")

    // Các lớp con chứa dữ liệu cụ thể
    data class EveryXDays(val days: Int = 1) : Frequency("Every $days days") {
        // No-argument constructor for Firestore
        constructor() : this(1)
    }
    
    data class SpecificDaysOfWeek(val days: List<DayOfWeek> = emptyList()) : Frequency(
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
    ) {
        // No-argument constructor for Firestore
        constructor() : this(emptyList())
    }

    data class EveryXWeeks(val weeks: Int = 1, val days: List<DayOfWeek> = emptyList()) : Frequency(
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
    ) {
        // No-argument constructor for Firestore
        constructor() : this(1, emptyList())
    }

    companion object {
        // Chuyển đổi từ chuỗi sang đối tượng
        // Logic này sẽ cần phức tạp hơn, tạm thởi để đơn giản
        fun fromDisplayText(text: String): Frequency {
            return when {
                text == "Every day" -> EveryDay()
                text == "Only as needed" -> AsNeeded()
                // ... cần logic phân tích chuỗi phức tạp hơn
                else -> EveryDay() // Mặc định
            }
        }
    }
}
