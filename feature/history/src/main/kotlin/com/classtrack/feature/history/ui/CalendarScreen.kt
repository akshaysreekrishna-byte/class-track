package com.classtrack.feature.history.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val Active = Color(0xFF6750A4)
private val Safe = Color(0xFF22C55E)
private val Critical = Color(0xFFBA1A1A)
private val Cancelled = Color(0xFFF59E0B)
private val PendingColor = Color(0xFF9CA3AF)
private val CardSurface = Color(0xFFFFFFFF)

/**
 * Structural placeholder for the Calendar tab.
 * Shows a monthly grid + daily timetable; self-contained (no :app imports).
 */
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { MonthlyCalendarGrid() }
        item { DailyScheduleHeader() }
        items(previewSchedule.size) { i -> TimetableRow(previewSchedule[i]) }
    }
}

@Composable
private fun MonthlyCalendarGrid() {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(0.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("September 2024", style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp))
            DayOfWeekRow()
            CalendarDaysGrid()
        }
    }
}

@Composable
private fun DayOfWeekRow() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        listOf("S","M","T","W","T","F","S").forEach { day ->
            Text(day, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CalendarDaysGrid() {
    val weeks = listOf(
        listOf("","","","1","2","3","4"),
        listOf("5","6","7","8","9","10","11"),
        listOf("12","13","14","15","16","17","18"),
        listOf("19","20","21","22","23","24","25"),
    )
    weeks.forEach { week ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            week.forEach { day -> CalendarDayCell(day, isSelected = day == "12") }
        }
    }
}

@Composable
private fun RowScope.CalendarDayCell(day: String, isSelected: Boolean) {
    Box(modifier = Modifier.weight(1f).aspectRatio(1f).clip(CircleShape)
        .background(if (isSelected) Active else Color.Transparent),
        contentAlignment = Alignment.Center) {
        if (day.isNotBlank()) {
            Text(day, style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun DailyScheduleHeader() {
    Column {
        Text("Thursday", style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary)
        Text("September 12, 2024", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun TimetableRow(item: ScheduleItem) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(1.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(width = 4.dp, height = 48.dp).background(item.color))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.time, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline)
                Text(item.subject, style = MaterialTheme.typography.titleMedium)
                Text(item.status, style = MaterialTheme.typography.labelSmall, color = item.color)
            }
        }
    }
}

private data class ScheduleItem(val time: String, val subject: String, val status: String, val color: Color)
private val previewSchedule = listOf(
    ScheduleItem("09:00 AM", "Economics", "Present", Safe),
    ScheduleItem("11:00 AM", "Discrete Math", "Absent", Critical),
    ScheduleItem("02:00 PM", "Workshop", "Cancelled", Cancelled),
    ScheduleItem("04:00 PM", "Lab", "Pending", PendingColor),
)

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FF)
@Composable
private fun CalendarScreenPreview() {
    MaterialTheme { CalendarScreen() }
}
