package com.attendease.core.domain.usecase

import com.attendease.core.domain.model.AttendanceStatus
import com.attendease.core.domain.model.AttendanceSummary
import com.attendease.core.domain.repository.AttendanceRepository
import com.attendease.core.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

import javax.inject.Inject

class GetAttendanceSummaryUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(): Flow<List<AttendanceSummary>> {
        return combine(
            subjectRepository.getAllSubjects(),
            attendanceRepository.getAllRecords()
        ) { subjectsResult, recordsResult ->
            val subjects = subjectsResult.getOrDefault(emptyList())
            val records = recordsResult.getOrDefault(emptyList())
            
            subjects.map { subject ->
                val subjectRecords = records.filter { it.subjectId == subject.id }
                val present = subjectRecords.count { it.status == AttendanceStatus.PRESENT }
                val absent = subjectRecords.count { it.status == AttendanceStatus.ABSENT }
                val total = present + absent
                val percentage = if (total == 0) 0.0 else (present.toDouble() / total.toDouble()) * 100.0

                AttendanceSummary(
                    subject = subject,
                    totalClasses = total,
                    presentClasses = present,
                    absentClasses = absent,
                    currentPercentage = percentage
                )
            }
        }
    }
}
