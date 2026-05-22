package com.classtrack.feature.subjects.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Dialog for creating a new [AcademicTerm] with a user-supplied name.
 */
@Composable
fun CreateSemesterDialog(
    onConfirm: (name: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var semesterName by remember { mutableStateOf("") }
    val isValid = semesterName.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Create Semester",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter a name for your new semester (e.g. \"Semester 3\").",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = semesterName,
                    onValueChange = { semesterName = it },
                    label = { Text("Semester Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = semesterName.isNotBlank() && semesterName.length > 50,
                    supportingText = if (semesterName.length > 50) {
                        { Text("Max 50 characters") }
                    } else null,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(semesterName) },
                enabled = isValid && semesterName.length <= 50,
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
