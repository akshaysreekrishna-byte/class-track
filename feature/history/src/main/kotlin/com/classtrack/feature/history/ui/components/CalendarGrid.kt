package com.classtrack.feature.history.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.classtrack.core.ui.components.StatusDot
import com.classtrack.feature.history.ui.state.CalendarDayUiModel

private val DayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")

/**
 * 7-column calendar grid.
 * Day cells show the date number + up to 3 status dots for multi-class days.
 * The selected day is highlighted with a primary-container filled circle.
 */
@Composable
fun CalendarGrid(
    days: List<CalendarDayUiModel>,
    onDaySelected: (CalendarDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            DayHeaderRow()
            CalendarDayRows(days = days, onDaySelected = onDaySelected)
        }
    }
}

@Composable
private fun DayHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        DayHeaders.forEach { header ->
            Text(
                text = header,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CalendarDayRows(
    days: List<CalendarDayUiModel>,
    onDaySelected: (CalendarDayUiModel) -> Unit,
) {
    val weeks = days.chunked(7)
    weeks.forEach { week ->
        Row(modifier = Modifier.fillMaxWidth()) {
            week.forEach { day ->
                CalendarDayCell(
                    day = day,
                    onDaySelected = onDaySelected,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: CalendarDayUiModel,
    onDaySelected: (CalendarDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = when {
        day.isSelected -> MaterialTheme.colorScheme.primaryContainer
        day.isToday -> MaterialTheme.colorScheme.secondaryContainer
        else -> Color.Transparent
    }
    val textColor = when {
        day.isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        !day.isCurrentMonth -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.onSurface
    }
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(enabled = day.isCurrentMonth) { onDaySelected(day) }
            .semantics {
                contentDescription = buildString {
                    append("${day.dayOfMonth}")
                    if (day.statusDots.isNotEmpty()) append(", ${day.statusDots.size} classes")
                    if (day.isSelected) append(", selected")
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (day.isCurrentMonth || day.statusDots.isNotEmpty()) "${day.dayOfMonth}" else "",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (day.isSelected || day.isToday) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
        )
        if (day.statusDots.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                day.statusDots.forEach { status -> StatusDot(status = status) }
            }
        }
    }
}
