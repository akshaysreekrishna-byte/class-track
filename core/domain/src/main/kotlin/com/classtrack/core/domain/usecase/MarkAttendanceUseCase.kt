package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MarkAttendanceUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(record: AttendanceRecord) {
        val now = Clock.System.now()
        val recordTime = Instant.fromEpochMilliseconds(record.timestamp)
        
        if (recordTime > now) {
            throw IllegalArgumentException("Cannot mark attendance in the future")
        }

        // If it's a manual record or not tied to a slot, just insert it
        if (!record.isAutoMarked || record.scheduleSlotId == null) {
            attendanceRepository.insertRecord(record)
            return
        }

        // It's an auto-marked record tied to a slot. Check for collisions on the same calendar day.
        val existingRecords = attendanceRepository.getRecordsForSlot(record.scheduleSlotId).firstOrNull() ?: emptyList()
        
        val recordDate = recordTime.toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        val collision = existingRecords.find { 
            Instant.fromEpochMilliseconds(it.timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date == recordDate
        }

        if (collision != null) {
            if (!collision.isAutoMarked) {
                // Manual wins over Auto. Ignore insertion.
                return
            } else {
                // Another auto record exists? Might want to update or ignore. Let's just ignore to prevent spam.
                return
            }
        }

        attendanceRepository.insertRecord(record)
    }
}
