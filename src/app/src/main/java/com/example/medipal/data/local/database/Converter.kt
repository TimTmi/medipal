package com.example.medipal.data.local.database

import androidx.room.TypeConverter
import com.example.medipal.domain.model.Frequency
import java.time.DayOfWeek

class Converters {
    /**
     * Chuyển đổi một đối tượng Frequency thành một String để lưu vào database.
     * Chúng ta sẽ lưu dưới dạng JSON string để có thể serialize/deserialize các data class.
     */
    @TypeConverter
    fun fromFrequency(frequency: Frequency): String {
        return when (frequency) {
            is Frequency.EveryDay -> "EVERY_DAY"
            is Frequency.AsNeeded -> "AS_NEEDED"
            is Frequency.EveryXDays -> "EVERY_X_DAYS:${frequency.days}"
            is Frequency.SpecificDaysOfWeek -> {
                val daysString = frequency.days.joinToString(",") { it.name }
                "SPECIFIC_DAYS:$daysString"
            }
            is Frequency.EveryXWeeks -> {
                val daysString = frequency.days.joinToString(",") { it.name }
                "EVERY_X_WEEKS:${frequency.weeks}:$daysString"
            }
        }
    }

    /**
     * Chuyển đổi một String từ database trở lại thành đối tượng Frequency.
     */
    @TypeConverter
    fun toFrequency(value: String): Frequency {
        return when {
            value == "EVERY_DAY" -> Frequency.EveryDay
            value == "AS_NEEDED" -> Frequency.AsNeeded
            value.startsWith("EVERY_X_DAYS:") -> {
                val days = value.substringAfter("EVERY_X_DAYS:").toIntOrNull() ?: 2
                Frequency.EveryXDays(days)
            }
            value.startsWith("SPECIFIC_DAYS:") -> {
                val daysString = value.substringAfter("SPECIFIC_DAYS:")
                val days = if (daysString.isEmpty()) {
                    emptySet()
                } else {
                    daysString.split(",").mapNotNull { dayName ->
                        try {
                            DayOfWeek.valueOf(dayName)
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    }.toSet()
                }
                Frequency.SpecificDaysOfWeek(days)
            }
            value.startsWith("EVERY_X_WEEKS:") -> {
                val parts = value.substringAfter("EVERY_X_WEEKS:").split(":")
                val weeks = parts.getOrNull(0)?.toIntOrNull() ?: 1
                val daysString = parts.getOrNull(1) ?: ""
                val days = if (daysString.isEmpty()) {
                    emptySet()
                } else {
                    daysString.split(",").mapNotNull { dayName ->
                        try {
                            DayOfWeek.valueOf(dayName)
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    }.toSet()
                }
                Frequency.EveryXWeeks(weeks, days)
            }
            else -> Frequency.EveryDay // Fallback
        }
    }
}