package com.classtrack.feature.history.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.classtrack.feature.history.ui.state.ScheduleEntryUiModel

/**
 * A timetable card for a single class session.
 * Uses a left-side 4dp accent bar in the semantic health color,
 * matching the Stitch calendar design precisely.
 *
 * @param entry   Pre-formatted [ScheduleEntryUiModel] from the ViewModel.
 * @param onEdit  Callback when the user taps the edit icon button.
 */
@Composable
fun TimetableEntryCard(
    entry: ScheduleEntryUiModel,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = "${entry.subjectName} at ${entry.timeLabel}: ${entry.statusLabel}"
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = description },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AccentBar(color = entry.status.toColor())
            EntryBody(
                entry = entry,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
            )
            IconButton(onClick = { onEdit(entry.recordId) }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit attendance for ${entry.subjectName}",
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
private fun AccentBar(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(56.dp)
            .background(color),
    )
}

@Composable
private fun EntryBody(
    entry: ScheduleEntryUiModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = entry.timeLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = entry.subjectName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 2.dp),
        )
        Text(
            text = entry.statusLabel,
            style = MaterialTheme.typography.labelSmall,
            color = entry.status.toColor(),
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
