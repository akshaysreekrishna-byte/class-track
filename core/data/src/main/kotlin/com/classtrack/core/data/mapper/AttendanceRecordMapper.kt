package com.classtrack.core.data.mapper

import com.classtrack.core.data.local.entity.AttendanceRecordEntity
import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.model.AttendanceStatus

fun AttendanceRecordEntity.toDomain(): AttendanceRecord {
    return AttendanceRecord(
        id = this.id,
        subjectId = this.subjectId,
        scheduleSlotId = this.scheduleSlotId,
        timestamp = this.timestamp,
        status = try { AttendanceStatus.valueOf(this.status) } catch (e: Exception) { AttendanceStatus.ABSENT },
        isAutoMarked = this.isAutoMarked,
        notes = this.notes
    )
}

fun AttendanceRecord.toEntity(): AttendanceRecordEntity {
    return AttendanceRecordEntity(
        id = this.id,
        subjectId = this.subjectId,
        scheduleSlotId = this.scheduleSlotId,
        timestamp = this.timestamp,
        status = this.status.name,
        isAutoMarked = this.isAutoMarked,
        notes = this.notes
    )
}
