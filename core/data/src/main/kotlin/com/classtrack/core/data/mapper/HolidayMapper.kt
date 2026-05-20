package com.classtrack.core.data.mapper

import com.classtrack.core.data.local.entity.HolidayEntity
import com.classtrack.core.domain.model.Holiday
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.atStartOfDayIn

fun HolidayEntity.toDomain(): Holiday {
    return Holiday(
        id = this.id,
        termId = this.termId,
        name = this.name,
        dateTimestamp = this.dateTimestamp
    )
}

fun Holiday.toEntity(): HolidayEntity {
    // Normalize to Midnight UTC for precise date-to-date equality matching during Geofencing
    val instant = Instant.fromEpochMilliseconds(this.dateTimestamp)
    val localDate = instant.toLocalDateTime(TimeZone.UTC).date
    
    val normalizedTimestamp = localDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

    return HolidayEntity(
        id = this.id,
        termId = this.termId,
        name = this.name,
        dateTimestamp = normalizedTimestamp
    )
}
