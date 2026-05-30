package com.classtrack.feature.subjects.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.classtrack.feature.subjects.ui.components.EmptySubjectScreen
import com.classtrack.feature.subjects.ui.components.NoSemesterScreen
import com.classtrack.feature.subjects.ui.components.SemesterSelectorMenu
import com.classtrack.feature.subjects.ui.components.SubjectCard
import com.classtrack.feature.subjects.ui.dialogs.AddEditSubjectDialog
import com.classtrack.feature.subjects.ui.dialogs.CreateSemesterDialog
import com.classtrack.feature.subjects.ui.dialogs.DeleteConfirmDialog
import com.classtrack.feature.subjects.ui.state.SubjectDialogState
import com.classtrack.feature.subjects.ui.state.SubjectUiItem
import com.classtrack.feature.subjects.ui.state.SubjectUiState

/**
 * Entry-point composable for the Subjects tab.
 * Retrieves the [SubjectViewModel] via [hiltViewModel] and delegates rendering
 * to stateless inner composables.
 */
@Composable
fun SubjectListScreen(viewModel: SubjectViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()
    val currentTerm by viewModel.currentTerm.collectAsStateWithLifecycle()
    val allTerms by viewModel.allTerms.collectAsStateWithLifecycle()

    SubjectListContent(
        uiState = uiState,
        dialogState = dialogState,
        currentTermName = currentTerm?.name,
        allTerms = allTerms.map { it },
        onAddClick = viewModel::onAddSubjectClick,
        onEditClick = viewModel::onEditSubjectClick,
        onDeleteClick = { id, name -> viewModel.onDeleteSubjectClick(id, name) },
        onCreateSemesterClick = viewModel::onCreateSemesterClick,
        onSwitchTerm = viewModel::onSwitchTerm,
        onDismissDialog = viewModel::onDismissDialog,
        onConfirmAdd = viewModel::onConfirmAddSubject,
        onConfirmEdit = viewModel::onConfirmEditSubject,
        onConfirmDelete = viewModel::onConfirmDeleteSubject,
        onConfirmCreateSemester = viewModel::onConfirmCreateSemester,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectListContent(
    uiState: SubjectUiState,
    dialogState: SubjectDialogState,
    currentTermName: String?,
    allTerms: List<com.classtrack.core.domain.model.AcademicTerm>,
    onAddClick: () -> Unit,
    onEditClick: (SubjectUiItem) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    onCreateSemesterClick: () -> Unit,
    onSwitchTerm: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmAdd: (String, com.classtrack.core.domain.model.SubjectType, Float) -> Unit,
    onConfirmEdit: (SubjectUiItem) -> Unit,
    onConfirmDelete: (String) -> Unit,
    onConfirmCreateSemester: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            SubjectsTopBar(
                currentTermName = currentTermName,
                allTerms = allTerms,
                onSwitchTerm = onSwitchTerm,
                onCreateNewSemester = onCreateSemesterClick,
            )
        },
        floatingActionButton = {
            if (uiState is SubjectUiState.Success || uiState is SubjectUiState.Empty) {
                ExtendedFloatingActionButton(
                    onClick = onAddClick,
                    icon = { Icon(Icons.Filled.Add, "Add subject") },
                    text = { Text("Add Subject") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        },
    ) { innerPadding ->
        SubjectBody(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
            onAddClick = onAddClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onCreateSemesterClick = onCreateSemesterClick,
        )
    }

    ActiveDialog(
        dialogState = dialogState,
        onDismiss = onDismissDialog,
        onConfirmAdd = onConfirmAdd,
        onConfirmEdit = onConfirmEdit,
        onConfirmDelete = onConfirmDelete,
        onConfirmCreateSemester = onConfirmCreateSemester,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectsTopBar(
    currentTermName: String?,
    allTerms: List<com.classtrack.core.domain.model.AcademicTerm>,
    onSwitchTerm: (String) -> Unit,
    onCreateNewSemester: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Manage Subjects",
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        actions = {
            SemesterSelectorMenu(
                currentTerm = allTerms.find { it.name == currentTermName },
                allTerms = allTerms,
                onSelectTerm = onSwitchTerm,
                onCreateNewSemester = onCreateNewSemester,
            )
        },
    )
}

@Composable
private fun SubjectBody(
    uiState: SubjectUiState,
    modifier: Modifier,
    onAddClick: () -> Unit,
    onEditClick: (SubjectUiItem) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    onCreateSemesterClick: () -> Unit,
) {
    when (uiState) {
        SubjectUiState.Loading -> Unit
        SubjectUiState.NoSemester -> NoSemesterScreen(
            onCreateSemesterClick = onCreateSemesterClick,
            modifier = modifier,
        )
        SubjectUiState.Empty -> EmptySubjectScreen(
            onAddSubjectClick = onAddClick,
            modifier = modifier,
        )
        is SubjectUiState.Success -> SubjectList(
            subjects = uiState.subjects,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            modifier = modifier,
        )
        is SubjectUiState.Error -> Text(
            text = uiState.message,
            modifier = modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun SubjectList(
    subjects: List<SubjectUiItem>,
    onEditClick: (SubjectUiItem) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(subjects, key = { it.id }) { subject ->
            SubjectCard(
                subject = subject,
                onEditClick = { onEditClick(subject) },
                onDeleteClick = { onDeleteClick(subject.id, subject.name) }
            )
        }
    }
}

@Composable
private fun ActiveDialog(
    dialogState: SubjectDialogState,
    onDismiss: () -> Unit,
    onConfirmAdd: (String, com.classtrack.core.domain.model.SubjectType, Float) -> Unit,
    onConfirmEdit: (SubjectUiItem) -> Unit,
    onConfirmDelete: (String) -> Unit,
    onConfirmCreateSemester: (String) -> Unit,
) {
    when (dialogState) {
        SubjectDialogState.Hidden -> Unit
        SubjectDialogState.AddNew -> AddEditSubjectDialog(
            onConfirm = { name, type, pct -> onConfirmAdd(name, type, pct) },
            onDismiss = onDismiss,
        )
        is SubjectDialogState.Edit -> AddEditSubjectDialog(
            existingSubject = dialogState.subject,
            onConfirm = { name, type, pct ->
                onConfirmEdit(dialogState.subject.copy(name = name, type = type, minAttendancePercentage = pct))
            },
            onDismiss = onDismiss,
        )
        is SubjectDialogState.ConfirmDelete -> DeleteConfirmDialog(
            subjectName = dialogState.subjectName,
            onConfirm = { onConfirmDelete(dialogState.subjectId) },
            onDismiss = onDismiss,
        )
        SubjectDialogState.CreateSemester -> CreateSemesterDialog(
            onConfirm = onConfirmCreateSemester,
            onDismiss = onDismiss,
        )
    }
}
