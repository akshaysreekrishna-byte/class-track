package com.classtrack.feature.history.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.classtrack.feature.history.ui.components.CalendarGrid
import com.classtrack.feature.history.ui.components.MonthNavigationHeader
import com.classtrack.feature.history.ui.components.TimetableEntryCard
import com.classtrack.feature.history.ui.state.CalendarDayUiModel
import com.classtrack.feature.history.ui.state.CalendarUiState
import com.classtrack.feature.history.ui.state.ScheduleEntryUiModel

/**
 * Stateless root of the Calendar/History tab.
 * Delegates all state and events to [CalendarViewModel].
 */
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    val viewModel: CalendarViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarContent(
        state = uiState,
        onMonthPrevious = viewModel::onMonthPrevious,
        onMonthNext = viewModel::onMonthNext,
        onDaySelected = { day -> viewModel.onDateSelected(day.date) },
        onEditEntry = { /* TODO: navigate to mark-attendance flow */ },
        modifier = modifier,
    )
}

@Composable
private fun CalendarContent(
    state: CalendarUiState,
    onMonthPrevious: () -> Unit,
    onMonthNext: () -> Unit,
    onDaySelected: (CalendarDayUiModel) -> Unit,
    onEditEntry: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        CalendarUiState.Loading -> LoadingIndicator(modifier)
        is CalendarUiState.Success -> SuccessCalendar(
            state = state,
            onMonthPrevious = onMonthPrevious,
            onMonthNext = onMonthNext,
            onDaySelected = onDaySelected,
            onEditEntry = onEditEntry,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun SuccessCalendar(
    state: CalendarUiState.Success,
    onMonthPrevious: () -> Unit,
    onMonthNext: () -> Unit,
    onDaySelected: (CalendarDayUiModel) -> Unit,
    onEditEntry: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(key = "month_header") {
            MonthNavigationHeader(
                monthLabel = state.monthLabel,
                onPrevious = onMonthPrevious,
                onNext = onMonthNext,
            )
        }
        item(key = "calendar_grid") {
            CalendarGrid(
                days = state.calendarDays,
                onDaySelected = onDaySelected,
            )
        }
        if (state.selectedDate != null) {
            item(key = "schedule_header") {
                DayScheduleHeader(
                    label = state.selectedDateLabel,
                    count = state.scheduleForSelectedDate.size,
                )
            }
            if (state.scheduleForSelectedDate.isEmpty()) {
                item(key = "empty_day") { EmptyDayMessage() }
            } else {
                items(
                    items = state.scheduleForSelectedDate,
                    key = { it.recordId },
                ) { entry ->
                    TimetableEntryCard(entry = entry, onEdit = onEditEntry)
                }
            }
        }
    }
}

@Composable
private fun DayScheduleHeader(label: String, count: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (count > 0) {
            Text(
                text = "$count class${if (count == 1) "" else "es"}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun EmptyDayMessage(modifier: Modifier = Modifier) {
    Text(
        text = "No classes recorded for this day.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(vertical = 16.dp),
    )
}
