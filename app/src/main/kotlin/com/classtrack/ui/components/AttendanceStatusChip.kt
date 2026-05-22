package com.classtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.classtrack.ui.theme.AttendanceCancelled
import com.classtrack.ui.theme.AttendanceCritical
import com.classtrack.ui.theme.AttendancePending
import com.classtrack.ui.theme.AttendanceSafe
import com.classtrack.ui.theme.ClassTrackTheme

/** Attendance health states for status display. */
enum class AttendanceHealthStatus {
    SAFE,
    CRITICAL,
    CANCELLED,
    PENDING,
}

/**
 * Small status chip showing a color dot + semantic label.
 * Used in dashboard subject cards and calendar timetable items.
 */
@Composable
fun AttendanceStatusChip(
    status: AttendanceHealthStatus,
    modifier: Modifier = Modifier,
) {
    val (color, label) = status.toColorAndLabel()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatusDot(color = color)
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

@Composable
private fun StatusDot(color: Color) {
    Text(
        text = "",
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color),
    )
}

private fun AttendanceHealthStatus.toColorAndLabel(): Pair<Color, String> = when (this) {
    AttendanceHealthStatus.SAFE -> AttendanceSafe to "Safe to Bunk"
    AttendanceHealthStatus.CRITICAL -> AttendanceCritical to "Critical: Do Not Bunk"
    AttendanceHealthStatus.CANCELLED -> AttendanceCancelled to "Cancelled"
    AttendanceHealthStatus.PENDING -> AttendancePending to "Pending"
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FF)
@Composable
private fun AttendanceStatusChipPreview() {
    ClassTrackTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AttendanceHealthStatus.entries.forEach { status ->
                AttendanceStatusChip(status = status)
            }
        }
    }
}
