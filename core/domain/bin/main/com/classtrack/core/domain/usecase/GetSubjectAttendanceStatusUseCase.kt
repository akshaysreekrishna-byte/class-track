package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.logic.BunkCalculator
import com.classtrack.core.domain.model.AttendanceStatus
import com.classtrack.core.domain.model.SubjectAttendanceSummary
import com.classtrack.core.domain.repository.AttendanceRepository
import com.classtrack.core.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

class GetSubjectAttendanceStatusUseCase(
    private val subjectRepository: SubjectRepository,
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(subjectId: String): Flow<SubjectAttendanceSummary> {
        val subjectFlow = subjectRepository.getSubjectById(subjectId).filterNotNull()
        val presentCountFlow = attendanceRepository.getAttendanceCountForSubject(subjectId, AttendanceStatus.PRESENT)
        val absentCountFlow = attendanceRepository.getAttendanceCountForSubject(subjectId, AttendanceStatus.ABSENT)
        
        return combine(subjectFlow, presentCountFlow, absentCountFlow) { subject, presentCount, absentCount ->
            val totalClasses = presentCount + absentCount
            val percentage = BunkCalculator.calculatePercentage(presentCount, totalClasses)
            
            val safeBunks = BunkCalculator.calculateSafeBunks(presentCount, totalClasses, subject.minAttendancePercentage)
            val required = BunkCalculator.calculateRequiredClasses(presentCount, totalClasses, subject.minAttendancePercentage)
            
            val statusText = if (safeBunks > 0) {
                "You can bunk the next $safeBunks classes!"
            } else if (required > 0) {
                "You must attend the next $required classes!"
            } else {
                "You are exactly on track!"
            }
            
            val actionCount = if (safeBunks > 0) safeBunks else required

            SubjectAttendanceSummary(
                subject = subject,
                totalClasses = totalClasses,
                presentCount = presentCount,
                currentPercentage = percentage,
                statusText = statusText,
                actionCount = actionCount
            )
        }
    }
}
