package com.classtrack.feature.history.ui.state

import com.classtrack.core.ui.components.AttendanceHealthStatus
import kotlinx.datetime.LocalDate

/**
 * Represents a single cell in the month calendar grid.
 *
 * [statusDots] holds up to 3 health statuses representing classes on that day,
 * capped for visual clarity in compact cells.
 */
data class CalendarDayUiModel(
    val date: LocalDate,
    val dayOfMonth: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val statusDots: List<AttendanceHealthStatus>,
)

/** A single row in the selected day's timetable section. */
data class ScheduleEntryUiModel(
    val recordId: String,
    val timeLabel: String,
    val subjectName: String,
    val status: AttendanceHealthStatus,
    val statusLabel: String,
)

/**
 * Screen state for the Calendar tab.
 *
 * Success holds the full calendar grid plus the selected day's timetable.
 * The grid is derived from a pre-grouped [Map] for O(1) per-cell lookup.
 * All date/month formatting is done in the ViewModel — state holds strings only.
 */
sealed interface CalendarUiState {
    data object Loading : CalendarUiState
    data class Success(
        val monthLabel: String,
        val calendarDays: List<CalendarDayUiModel>,
        val selectedDate: LocalDate?,
        val selectedDateLabel: String,
        val scheduleForSelectedDate: List<ScheduleEntryUiModel>,
    ) : CalendarUiState
}
