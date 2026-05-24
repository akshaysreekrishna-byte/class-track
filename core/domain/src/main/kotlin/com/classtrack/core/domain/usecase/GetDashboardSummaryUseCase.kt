package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.logic.BunkCalculator
import com.classtrack.core.domain.model.AttendanceStatus
import com.classtrack.core.domain.model.SubjectAttendanceSummary
import com.classtrack.core.domain.repository.AttendanceRepository
import com.classtrack.core.domain.repository.SubjectRepository
import com.classtrack.core.domain.usecase.GetSubjectsForCurrentTermUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Emits [DashboardSummary] combining all subjects in the current term
 * with their live attendance counts from the repository.
 *
 * Zero-division guard: when Σtotal == 0 (first day of semester),
 * [DashboardSummary.overallPercentage] is explicitly set to null so the
 * ViewModel can map it to the [Empty] state without dividing by zero.
 */
class GetDashboardSummaryUseCase(
    private val getSubjectsForCurrentTermUseCase: GetSubjectsForCurrentTermUseCase,
    private val subjectRepository: SubjectRepository,
    private val attendanceRepository: AttendanceRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<DashboardSummary> =
        getSubjectsForCurrentTermUseCase().flatMapLatest { subjects ->
            if (subjects.isEmpty()) return@flatMapLatest flowOf(DashboardSummary.empty())
            buildCombinedFlow(subjects)
        }

    private fun buildCombinedFlow(
        subjects: List<com.classtrack.core.domain.model.Subject>,
    ): Flow<DashboardSummary> {
        val subjectFlows = subjects.map { subject ->
            val presentFlow = attendanceRepository
                .getAttendanceCountForSubject(subject.id, AttendanceStatus.PRESENT)
            val absentFlow = attendanceRepository
                .getAttendanceCountForSubject(subject.id, AttendanceStatus.ABSENT)
            combine(presentFlow, absentFlow) { present, absent ->
                buildSummary(subject, present, absent)
            }
        }
        return combine(subjectFlows) { summaries ->
            val list = summaries.toList()
            val totalPresent = list.sumOf { it.presentCount }
            val totalClasses = list.sumOf { it.totalClasses }
            // Zero-division guard: null signals "no data yet"
            val overall = if (totalClasses == 0) null
            else BunkCalculator.calculatePercentage(totalPresent, totalClasses)
            DashboardSummary(overallPercentage = overall, subjects = list)
        }
    }

    private fun buildSummary(
        subject: com.classtrack.core.domain.model.Subject,
        presentCount: Int,
        absentCount: Int,
    ): SubjectAttendanceSummary {
        val totalClasses = presentCount + absentCount
        val percentage = BunkCalculator.calculatePercentage(presentCount, totalClasses)
        val safeBunks = BunkCalculator.calculateSafeBunks(
            presentCount, totalClasses, subject.minAttendancePercentage
        )
        val required = BunkCalculator.calculateRequiredClasses(
            presentCount, totalClasses, subject.minAttendancePercentage
        )
        val statusText = when {
            safeBunks > 0 -> "You can bunk the next $safeBunks classes!"
            required > 0 -> "You must attend the next $required classes!"
            else -> "You are exactly on track!"
        }
        return SubjectAttendanceSummary(
            subject = subject,
            totalClasses = totalClasses,
            presentCount = presentCount,
            currentPercentage = percentage,
            statusText = statusText,
            actionCount = if (safeBunks > 0) safeBunks else required,
        )
    }
}

/**
 * Aggregated dashboard payload.
 * [overallPercentage] is null when no attendance has been recorded yet.
 */
data class DashboardSummary(
    val overallPercentage: Float?,
    val subjects: List<SubjectAttendanceSummary>,
) {
    companion object {
        fun empty() = DashboardSummary(overallPercentage = null, subjects = emptyList())
    }
}
