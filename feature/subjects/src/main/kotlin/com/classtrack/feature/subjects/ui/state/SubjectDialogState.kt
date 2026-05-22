package com.classtrack.feature.subjects.ui.state

import com.classtrack.core.domain.model.SubjectType

/** Controls the visibility and mode of overlay dialogs. */
sealed interface SubjectDialogState {
    data object Hidden : SubjectDialogState
    data object AddNew : SubjectDialogState
    data class Edit(val subject: SubjectUiItem) : SubjectDialogState
    data class ConfirmDelete(val subjectId: String, val subjectName: String) : SubjectDialogState
    data object CreateSemester : SubjectDialogState
}
