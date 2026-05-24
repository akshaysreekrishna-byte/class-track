package com.classtrack.feature.dashboard.ui.state

import com.classtrack.core.ui.components.AttendanceHealthStatus

/**
 * Flattened UI model for a single subject row in the dashboard.
 * Contains only pre-formatted display strings — no raw domain types.
 */
data class SubjectSummaryUiModel(
    val id: String,
    val name: String,
    val typeLabel: String,
    val percentage: Float,
    val healthStatus: AttendanceHealthStatus,
    val actionLabel: String,
)

/**
 * Banner shown when at least one subject is safe to skip.
 * Surfaces the single most impactful "free bunk" to the user.
 */
data class InsightBannerUiModel(
    val subjectName: String,
    val safeBunks: Int,
)

/**
 * Unidirectional data-flow state for the Dashboard screen.
 *
 * Loading     → initial skeleton state
 * NoSemester  → no academic term configured yet
 * Empty       → term exists but has zero subjects enrolled
 * NoData      → subjects exist but Σtotal == 0 (first day of semester)
 * Success     → live data with overall KPI + per-subject rows
 */
sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data object NoSemester : DashboardUiState
    data object Empty : DashboardUiState
    data object NoData : DashboardUiState
    data class Success(
        val overallPercentage: Float,
        val overallHealthStatus: AttendanceHealthStatus,
        val topInsight: InsightBannerUiModel?,
        val subjects: List<SubjectSummaryUiModel>,
    ) : DashboardUiState
}
