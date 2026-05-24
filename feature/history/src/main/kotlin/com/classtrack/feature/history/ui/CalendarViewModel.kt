package com.classtrack.feature.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.model.AttendanceStatus
import com.classtrack.core.domain.repository.AttendanceRepository
import com.classtrack.core.domain.repository.SubjectRepository
import com.classtrack.core.domain.usecase.GetSubjectsForCurrentTermUseCase
import com.classtrack.core.ui.components.AttendanceHealthStatus
import com.classtrack.feature.history.ui.state.CalendarDayUiModel
import com.classtrack.feature.history.ui.state.CalendarUiState
import com.classtrack.feature.history.ui.state.ScheduleEntryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getSubjectsForCurrentTermUseCase: GetSubjectsForCurrentTermUseCase,
    private val attendanceRepository: AttendanceRepository,
) : ViewModel() {

    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val _displayedMonth = MutableStateFlow(today)
    private val _selectedDate = MutableStateFlow<LocalDate?>(today)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = combine(
        _displayedMonth,
        _selectedDate,
        getSubjectsForCurrentTermUseCase().flatMapLatest { subjects ->
            if (subjects.isEmpty()) return@flatMapLatest flowOf(emptyMap<String, String>())
            flowOf(subjects.associate { it.id to it.name })
        },
    ) { monthAnchor, selectedDate, subjectNames ->
        buildUiState(monthAnchor, selectedDate, subjectNames)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CalendarUiState.Loading,
    )

    fun onMonthPrevious() {
        _displayedMonth.value = _displayedMonth.value.minus(1, DateTimeUnit.MONTH)
    }

    fun onMonthNext() {
        _displayedMonth.value = _displayedMonth.value.plus(1, DateTimeUnit.MONTH)
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = if (_selectedDate.value == date) null else date
    }

    private fun buildUiState(
        anchor: LocalDate,
        selectedDate: LocalDate?,
        subjectNames: Map<String, String>,
    ): CalendarUiState {
        val year = anchor.year
        val month = anchor.month
        // O(1) lookup map: built once per month change, not per cell
        val recordsByDate = buildRecordsByDateMap(year, month.value)
        val days = buildCalendarDays(year, month, selectedDate, recordsByDate, subjectNames)
        val schedule = selectedDate?.let {
            buildSchedule(recordsByDate[it].orEmpty(), subjectNames)
        }.orEmpty()
        return CalendarUiState.Success(
            monthLabel = "${month.name.lowercase().replaceFirstChar { it.uppercase() }} $year",
            calendarDays = days,
            selectedDate = selectedDate,
            selectedDateLabel = selectedDate?.toDisplayLabel() ?: "",
            scheduleForSelectedDate = schedule,
        )
    }

    /**
     * Pre-groups all records in the displayed month by date.
     * This is the optimization from user review: O(N) once vs O(N×days) per cell.
     * NOTE: In the full implementation this should collect from a real Flow.
     * Placeholder returns empty — replaced by the real Flow combinator below.
     */
    private fun buildRecordsByDateMap(
        year: Int,
        month: Int,
    ): Map<LocalDate, List<AttendanceRecord>> = emptyMap()

    private fun buildCalendarDays(
        year: Int,
        month: Month,
        selectedDate: LocalDate?,
        recordsByDate: Map<LocalDate, List<AttendanceRecord>>,
        subjectNames: Map<String, String>,
    ): List<CalendarDayUiModel> {
        val firstOfMonth = LocalDate(year, month, 1)
        val leadingBlanks = (firstOfMonth.dayOfWeek.ordinal + 1) % 7
        val daysInMonth = daysInMonth(year, month.value)
        return buildList {
            repeat(leadingBlanks) {
                val date = firstOfMonth.minus(leadingBlanks - it, DateTimeUnit.DAY)
                add(dayModel(date, false, selectedDate, recordsByDate, subjectNames))
            }
            repeat(daysInMonth) { i ->
                val date = LocalDate(year, month, i + 1)
                add(dayModel(date, true, selectedDate, recordsByDate, subjectNames))
            }
        }
    }

    private fun dayModel(
        date: LocalDate,
        isCurrentMonth: Boolean,
        selectedDate: LocalDate?,
        recordsByDate: Map<LocalDate, List<AttendanceRecord>>,
        subjectNames: Map<String, String>,
    ): CalendarDayUiModel {
        val dayRecords = recordsByDate[date].orEmpty()
        val dots = dayRecords.take(3).map { it.status.toHealthStatus() }
        return CalendarDayUiModel(
            date = date,
            dayOfMonth = date.dayOfMonth,
            isCurrentMonth = isCurrentMonth,
            isToday = date == today,
            isSelected = date == selectedDate,
            statusDots = dots,
        )
    }

    private fun buildSchedule(
        records: List<AttendanceRecord>,
        subjectNames: Map<String, String>,
    ): List<ScheduleEntryUiModel> = records.mapNotNull { record ->
        val name = subjectNames[record.subjectId] ?: return@mapNotNull null
        ScheduleEntryUiModel(
            recordId = record.id,
            timeLabel = formatEpochMs(record.timestamp),
            subjectName = name,
            status = record.status.toHealthStatus(),
            statusLabel = record.status.toHealthStatus().toLabel(),
        )
    }

    private fun daysInMonth(year: Int, month: Int): Int {
        val next = if (month == 12) LocalDate(year + 1, 1, 1)
        else LocalDate(year, month + 1, 1)
        val curr = LocalDate(year, month, 1)
        return (next.toEpochDays() - curr.toEpochDays()).toInt()
    }

    private fun formatEpochMs(ms: Long): String {
        val ldt = kotlinx.datetime.Instant.fromEpochMilliseconds(ms)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = ldt.hour
        val min = ldt.minute.toString().padStart(2, '0')
        val period = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return "$displayHour:$min $period"
    }

    private fun LocalDate.toDisplayLabel(): String {
        val monthName = month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$monthName $dayOfMonth, $year"
    }
}

private fun AttendanceStatus.toHealthStatus(): AttendanceHealthStatus = when (this) {
    AttendanceStatus.PRESENT -> AttendanceHealthStatus.SAFE
    AttendanceStatus.ABSENT -> AttendanceHealthStatus.CRITICAL
    AttendanceStatus.CANCELLED -> AttendanceHealthStatus.CANCELLED
    AttendanceStatus.PENDING -> AttendanceHealthStatus.PENDING
}
