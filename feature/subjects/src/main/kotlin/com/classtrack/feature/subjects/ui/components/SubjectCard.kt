package com.classtrack.feature.subjects.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.classtrack.core.ui.components.AttendanceStatusChip
import com.classtrack.core.ui.theme.Primary
import com.classtrack.core.ui.theme.Secondary
import com.classtrack.core.ui.theme.Tertiary
import com.classtrack.feature.subjects.ui.state.SubjectAccentColor
import com.classtrack.feature.subjects.ui.state.SubjectUiItem

/**
 * Subject list card with a bold left accent bar, type chip, subject name,
 * and — when attendance data exists — an inline analytics row showing
 * the current percentage + health status chip + stacked bar breakdown.
 */
@Composable
fun SubjectCard(
    subject: SubjectUiItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                AccentBar(color = subject.accentColor.toColor())
                CardContent(
                    subject = subject,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                )
            }
            if (subject.totalCount > 0) {
                SubjectAnalyticsBar(
                    presentCount = subject.presentCount,
                    totalCount = subject.totalCount,
                )
            }
        }
    }
}

@Composable
private fun AccentBar(color: Color) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .fillMaxHeight()
            .background(color),
    )
}

@Composable
private fun CardContent(
    subject: SubjectUiItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        SubjectInfo(subject = subject, modifier = Modifier.weight(1f))
        CardActions(onEditClick = onEditClick, onDeleteClick = onDeleteClick)
    }
}

@Composable
private fun SubjectInfo(subject: SubjectUiItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        SubjectTypeChip(type = subject.type)
        Text(
            text = subject.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
        )
        subject.healthStatus?.let { status ->
            AttendanceStatusChip(
                status = status,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        subject.attendancePercentage?.let { pct ->
            Text(
                text = "${pct.toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = subject.healthStatus?.toColor() ?: MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun CardActions(onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row {
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit subject",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete subject",
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

private fun SubjectAccentColor.toColor(): Color = when (this) {
    SubjectAccentColor.PRIMARY -> Primary
    SubjectAccentColor.SECONDARY -> Secondary
    SubjectAccentColor.TERTIARY -> Tertiary
}
