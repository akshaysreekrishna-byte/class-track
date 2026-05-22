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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.classtrack.core.domain.model.SubjectType
import com.classtrack.feature.subjects.ui.state.SubjectAccentColor
import com.classtrack.feature.subjects.ui.state.SubjectUiItem

private val Primary = Color(0xFF6750A4)
private val Secondary = Color(0xFF625B71)
private val Tertiary = Color(0xFF7D5260)
private val SurfaceContainerLowest = Color(0xFFFFFFFF)

/**
 * Subject list card matching the Stitch design.
 * Features a bold 4dp left accent bar in the subject's cycled accent color,
 * a type chip, the subject name, and edit/delete action buttons.
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
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
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
    }
}

@Composable
private fun AccentBar(color: Color) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .fillMaxHeight()
            .background(color)
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
        Column(modifier = Modifier.weight(1f)) {
            SubjectTypeChip(type = subject.type)
            Text(
                text = subject.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        CardActions(onEditClick = onEditClick, onDeleteClick = onDeleteClick)
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

@Preview(showBackground = true, backgroundColor = 0xFFF1ECF7)
@Composable
private fun SubjectCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        ) {
            SubjectCard(
                subject = SubjectUiItem("1", "Data Structures", SubjectType.THEORY, 75f, SubjectAccentColor.PRIMARY),
                onEditClick = {}, onDeleteClick = {},
            )
            SubjectCard(
                subject = SubjectUiItem("2", "OS Lab", SubjectType.LAB, 75f, SubjectAccentColor.SECONDARY),
                onEditClick = {}, onDeleteClick = {},
            )
            SubjectCard(
                subject = SubjectUiItem("3", "Computer Networks", SubjectType.THEORY, 75f, SubjectAccentColor.TERTIARY),
                onEditClick = {}, onDeleteClick = {},
            )
        }
    }
}
