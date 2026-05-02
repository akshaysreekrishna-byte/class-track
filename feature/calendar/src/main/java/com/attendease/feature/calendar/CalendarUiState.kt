package com.attendease.feature.calendar

import com.attendease.core.domain.model.AttendanceRecord
import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val recordsForDate: List<AttendanceRecord> = emptyList(),
    val error: String? = null
)
