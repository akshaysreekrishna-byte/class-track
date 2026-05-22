package com.classtrack.feature.subjects.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.classtrack.core.domain.model.SubjectType
import com.classtrack.feature.subjects.ui.state.SubjectUiItem

/**
 * Shared dialog for creating a new subject and editing an existing one.
 * Pre-populates fields when [existingSubject] is non-null (edit mode).
 */
@Composable
fun AddEditSubjectDialog(
    existingSubject: SubjectUiItem? = null,
    onConfirm: (name: String, type: SubjectType, minPct: Float) -> Unit,
    onDismiss: () -> Unit,
) {
    val isEditMode = existingSubject != null
    var subjectName by remember { mutableStateOf(existingSubject?.name ?: "") }
    var selectedType by remember { mutableStateOf(existingSubject?.type ?: SubjectType.THEORY) }
    var minPct by remember { mutableFloatStateOf(existingSubject?.minAttendancePercentage ?: 75f) }

    val isNameValid = subjectName.isNotBlank() && subjectName.length <= 50

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "Edit Subject" else "Add Subject",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            DialogContent(
                subjectName = subjectName,
                onNameChange = { subjectName = it },
                selectedType = selectedType,
                onTypeChange = { selectedType = it },
                minPct = minPct,
                onMinPctChange = { minPct = it },
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(subjectName.trim(), selectedType, minPct) },
                enabled = isNameValid,
            ) {
                Text(if (isEditMode) "Save" else "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun DialogContent(
    subjectName: String,
    onNameChange: (String) -> Unit,
    selectedType: SubjectType,
    onTypeChange: (SubjectType) -> Unit,
    minPct: Float,
    onMinPctChange: (Float) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = subjectName,
            onValueChange = onNameChange,
            label = { Text("Subject Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = subjectName.isNotBlank() && subjectName.length > 50,
            supportingText = if (subjectName.length > 50) {
                { Text("Max 50 characters") }
            } else null,
        )
        SubjectTypeSegmentedRow(selectedType = selectedType, onTypeChange = onTypeChange)
        AttendanceThresholdSlider(minPct = minPct, onMinPctChange = onMinPctChange)
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun SubjectTypeSegmentedRow(
    selectedType: SubjectType,
    onTypeChange: (SubjectType) -> Unit,
) {
    val types = SubjectType.entries
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        types.forEachIndexed { index, type ->
            SegmentedButton(
                selected = selectedType == type,
                onClick = { onTypeChange(type) },
                shape = SegmentedButtonDefaults.itemShape(index, types.size),
                label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
            )
        }
    }
}

@Composable
private fun AttendanceThresholdSlider(minPct: Float, onMinPctChange: (Float) -> Unit) {
    Column {
        Text(
            text = "Minimum Attendance: ${minPct.toInt()}%",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
            value = minPct,
            onValueChange = onMinPctChange,
            valueRange = 50f..100f,
            steps = 9, // 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100
        )
    }
}
