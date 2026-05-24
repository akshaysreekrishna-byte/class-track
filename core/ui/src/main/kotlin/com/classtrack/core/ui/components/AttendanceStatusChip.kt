package com.classtrack.core.ui.components

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * A compact status chip: colored dot + semantic label text.
 * Used in subject cards, dashboard rows, and the calendar timetable.
 *
 * @param status The [AttendanceHealthStatus] driving color and label text.
 */
@Composable
fun AttendanceStatusChip(
    status: AttendanceHealthStatus,
    modifier: Modifier = Modifier,
) {
    val color = status.toColor()
    val label = status.toLabel()

    Row(
        modifier = modifier.semantics { contentDescription = label },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "",
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

/**
 * A small colored dot indicator — used for calendar day cells
 * where multiple classes per day need to be represented compactly.
 */
@Composable
fun StatusDot(
    status: AttendanceHealthStatus,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "",
        modifier = modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(status.toColor()),
    )
}
