package com.classtrack.feature.subjects.ui.state

import com.classtrack.core.domain.model.SubjectType

/** Accent color cycling for subject cards — Primary → Secondary → Tertiary. */
enum class SubjectAccentColor { PRIMARY, SECONDARY, TERTIARY }

/** Flattened UI representation of a Subject for the list screen. */
data class SubjectUiItem(
    val id: String,
    val name: String,
    val type: SubjectType,
    val minAttendancePercentage: Float,
    val accentColor: SubjectAccentColor,
)

/** Overall screen state. */
sealed interface SubjectUiState {
    data object Loading : SubjectUiState
    /** No AcademicTerm has been created yet. */
    data object NoSemester : SubjectUiState
    /** Term exists but has no subjects yet. */
    data object Empty : SubjectUiState
    data class Success(val subjects: List<SubjectUiItem>) : SubjectUiState
    data class Error(val message: String) : SubjectUiState
}
