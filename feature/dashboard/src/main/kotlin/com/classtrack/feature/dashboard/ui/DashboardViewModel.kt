package com.classtrack.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classtrack.core.domain.model.SubjectAttendanceSummary
import com.classtrack.core.domain.usecase.DashboardSummary
import com.classtrack.core.domain.usecase.GetDashboardSummaryUseCase
import com.classtrack.core.domain.repository.AcademicTermRepository
import com.classtrack.core.ui.components.AttendanceHealthStatus
import com.classtrack.feature.dashboard.ui.state.DashboardUiState
import com.classtrack.feature.dashboard.ui.state.InsightBannerUiModel
import com.classtrack.feature.dashboard.ui.state.SubjectSummaryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val termRepository: AcademicTermRepository,
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        termRepository.getCurrentTerm(),
        getDashboardSummaryUseCase(),
    ) { term, summary ->
        mapToUiState(term, summary)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState.Loading,
    )

    private fun mapToUiState(
        term: com.classtrack.core.domain.model.AcademicTerm?,
        summary: DashboardSummary,
    ): DashboardUiState {
        if (term == null) return DashboardUiState.NoSemester
        if (summary.subjects.isEmpty()) return DashboardUiState.Empty
        val overallPct = summary.overallPercentage ?: return DashboardUiState.NoData
        return DashboardUiState.Success(
            overallPercentage = overallPct,
            overallHealthStatus = resolveOverallHealth(overallPct, summary.subjects),
            topInsight = findTopInsight(summary.subjects),
            subjects = summary.subjects.map { it.toUiModel() },
        )
    }

    private fun resolveOverallHealth(
        overall: Float,
        subjects: List<SubjectAttendanceSummary>,
    ): AttendanceHealthStatus {
        val avgTarget = subjects.map { it.subject.minAttendancePercentage }.average().toFloat()
        return when {
            overall >= avgTarget + 5f -> AttendanceHealthStatus.SAFE
            overall >= avgTarget -> AttendanceHealthStatus.PENDING
            else -> AttendanceHealthStatus.CRITICAL
        }
    }

    private fun findTopInsight(subjects: List<SubjectAttendanceSummary>): InsightBannerUiModel? {
        val safeSubject = subjects.firstOrNull { it.actionCount > 0 && it.currentPercentage >= it.subject.minAttendancePercentage }
        return safeSubject?.let { InsightBannerUiModel(it.subject.name, it.actionCount) }
    }

    private fun SubjectAttendanceSummary.toUiModel(): SubjectSummaryUiModel {
        val isSafe = currentPercentage >= subject.minAttendancePercentage
        val health = when {
            isSafe && actionCount > 0 -> AttendanceHealthStatus.SAFE
            !isSafe && actionCount > 0 -> AttendanceHealthStatus.CRITICAL
            else -> AttendanceHealthStatus.PENDING
        }
        val actionLabel = when {
            health == AttendanceHealthStatus.SAFE -> "Skip $actionCount more"
            health == AttendanceHealthStatus.CRITICAL -> "Attend $actionCount classes"
            else -> "Exactly on track"
        }
        return SubjectSummaryUiModel(
            id = subject.id,
            name = subject.name,
            typeLabel = subject.type.name.lowercase().replaceFirstChar { it.uppercase() },
            percentage = currentPercentage,
            healthStatus = health,
            actionLabel = actionLabel,
        )
    }
}
