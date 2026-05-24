package com.classtrack.feature.dashboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.classtrack.core.ui.components.AttendanceStatusChip
import com.classtrack.feature.dashboard.ui.state.SubjectSummaryUiModel

/**
 * Per-subject row card matching the Stitch outlined card design.
 * Left accent bar in the subject's semantic health color, name + chip,
 * and the attendance percentage right-aligned.
 *
 * @param subject Pre-formatted [SubjectSummaryUiModel] from the ViewModel.
 */
@Composable
fun SubjectSummaryCard(
    subject: SubjectSummaryUiModel,
    modifier: Modifier = Modifier,
) {
    val description = "${subject.name}: ${subject.percentage.toInt()}%. ${subject.actionLabel}"
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
        Row(modifier = Modifier.fillMaxWidth()) {
            AccentBar(color = subject.healthStatus.toColor())
            CardBody(subject = subject, modifier = Modifier.weight(1f))
            PercentageLabel(subject = subject)
        }
    }
}

@Composable
private fun AccentBar(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .fillMaxHeight()
            .background(color),
    )
}

@Composable
private fun CardBody(subject: SubjectSummaryUiModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Text(
            text = subject.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        AttendanceStatusChip(
            status = subject.healthStatus,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun PercentageLabel(subject: SubjectSummaryUiModel) {
    Column(
        modifier = Modifier.padding(end = 16.dp, top = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "${subject.percentage.toInt()}%",
            style = MaterialTheme.typography.headlineMedium,
            color = subject.healthStatus.toColor(),
        )
        Text(
            text = subject.actionLabel,
            style = MaterialTheme.typography.labelSmall,
            color = subject.healthStatus.toColor().copy(alpha = 0.8f),
        )
    }
}
