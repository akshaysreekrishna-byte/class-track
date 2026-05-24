package com.classtrack.feature.subjects.ui.state

import com.classtrack.core.domain.model.SubjectType
import com.classtrack.core.ui.components.AttendanceHealthStatus

/** Accent color cycling for subject cards — Primary → Secondary → Tertiary. */
enum class SubjectAccentColor { PRIMARY, SECONDARY, TERTIARY }

/** Flattened UI representation of a Subject with live attendance analytics. */
data class SubjectUiItem(
    val id: String,
    val name: String,
    val type: SubjectType,
    val minAttendancePercentage: Float,
    val accentColor: SubjectAccentColor,
    // Analytics fields — null when no classes have been recorded yet
    val attendancePercentage: Float? = null,
    val healthStatus: AttendanceHealthStatus? = null,
    val actionLabel: String? = null,
    val presentCount: Int = 0,
    val totalCount: Int = 0,
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
