package com.attendease.core.domain.usecase

import com.attendease.core.domain.model.AttendanceRecord
import com.attendease.core.domain.repository.AttendanceRepository
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(record: AttendanceRecord): Result<Long> {
        return attendanceRepository.insertRecord(record)
    }
}
