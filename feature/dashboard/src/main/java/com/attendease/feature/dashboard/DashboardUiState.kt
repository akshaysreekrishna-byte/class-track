package com.attendease.feature.dashboard

import com.attendease.core.domain.model.AttendanceSummary

data class DashboardUiState(
    val isLoading: Boolean = false,
    val totalPercentage: Double = 0.0,
    val overallHealthText: String = "",
    val subjectSummaries: List<AttendanceSummary> = emptyList(),
    val error: String? = null
)
