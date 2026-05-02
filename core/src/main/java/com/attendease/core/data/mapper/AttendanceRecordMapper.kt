package com.attendease.core.data.mapper

import com.attendease.core.data.database.entity.AttendanceRecordEntity
import com.attendease.core.domain.model.AttendanceRecord
import com.attendease.core.domain.model.AttendanceStatus
import java.time.LocalDate

object AttendanceRecordMapper {
    fun AttendanceRecordEntity.toDomain(): AttendanceRecord {
        return AttendanceRecord(
            id = id,
            subjectId = subjectId,
            date = LocalDate.parse(date),
            status = AttendanceStatus.entries[statusOrdinal],
            isManualOverride = isManualOverride
        )
    }

    fun AttendanceRecord.toEntity(): AttendanceRecordEntity {
        return AttendanceRecordEntity(
            id = id,
            subjectId = subjectId,
            date = date.toString(),
            statusOrdinal = status.ordinal,
            isManualOverride = isManualOverride
        )
    }
}
