package com.classtrack.feature.subjects.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.classtrack.core.domain.model.AcademicTerm

/**
 * Displays the current semester name as a tappable [TextButton].
 * Tapping reveals a dropdown listing all semesters plus a
 * "＋ New Semester" option at the bottom.
 */
@Composable
fun SemesterSelectorMenu(
    currentTerm: AcademicTerm?,
    allTerms: List<AcademicTerm>,
    onSelectTerm: (String) -> Unit,
    onCreateNewSemester: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        TextButton(onClick = { expanded = true }) {
            Text(
                text = currentTerm?.name ?: "No Semester",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = "Select semester",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            allTerms.forEach { term ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = term.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (term.id == currentTerm?.id) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    },
                    onClick = {
                        expanded = false
                        onSelectTerm(term.id)
                    },
                )
            }
            HorizontalDivider()
            DropdownMenuItem(
                text = {
                    Text(
                        text = "+ New Semester",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                onClick = {
                    expanded = false
                    onCreateNewSemester()
                },
            )
        }
    }
}
