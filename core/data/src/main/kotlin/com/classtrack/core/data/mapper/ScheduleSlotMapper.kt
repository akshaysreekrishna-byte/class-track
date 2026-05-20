package com.classtrack.core.data.mapper

import com.classtrack.core.data.local.entity.ScheduleSlotEntity
import com.classtrack.core.domain.model.ScheduleSlot

fun ScheduleSlotEntity.toDomain(): ScheduleSlot {
    return ScheduleSlot(
        id = this.id,
        subjectId = this.subjectId,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime,
        endTime = this.endTime,
        roomNumber = this.roomNumber
    )
}

fun ScheduleSlot.toEntity(): ScheduleSlotEntity {
    val timeRegex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
    
    if (!timeRegex.matches(this.startTime)) {
        throw IllegalArgumentException("Invalid startTime format: \${this.startTime}. Must be HH:mm (24-hour zero-padded)")
    }
    if (!timeRegex.matches(this.endTime)) {
        throw IllegalArgumentException("Invalid endTime format: \${this.endTime}. Must be HH:mm (24-hour zero-padded)")
    }

    return ScheduleSlotEntity(
        id = this.id,
        subjectId = this.subjectId,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime,
        endTime = this.endTime,
        roomNumber = this.roomNumber
    )
}
