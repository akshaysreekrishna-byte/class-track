package com.attendease.core.domain.usecase

import com.attendease.core.domain.calculator.BunkCalculator
import com.attendease.core.domain.model.AttendanceStatus
import com.attendease.core.domain.model.BunkResult
import com.attendease.core.domain.model.Subject
import com.attendease.core.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculateBunkSafetyUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(subject: Subject): Flow<BunkResult> {
        return attendanceRepository.getRecordsForSubject(subject.id).map { result ->
            val records = result.getOrDefault(emptyList())
            val present = records.count { it.status == AttendanceStatus.PRESENT }
            val absent = records.count { it.status == AttendanceStatus.ABSENT }
            
            BunkCalculator.calculate(
                presentClasses = present,
                absentClasses = absent,
                subjectType = subject.type,
                targetPercentage = subject.requiredPercentage
            )
        }
    }
}
