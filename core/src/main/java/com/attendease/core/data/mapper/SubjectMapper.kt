package com.attendease.core.data.mapper

import com.attendease.core.data.database.entity.SubjectEntity
import com.attendease.core.domain.model.Subject
import com.attendease.core.domain.model.SubjectType

object SubjectMapper {
    fun SubjectEntity.toDomain(): Subject {
        return Subject(
            id = id,
            name = name,
            type = SubjectType.entries[typeOrdinal],
            requiredPercentage = requiredPercentage,
            geofenceLat = geofenceLat,
            geofenceLng = geofenceLng,
            geofenceRadius = geofenceRadius
        )
    }

    fun Subject.toEntity(): SubjectEntity {
        return SubjectEntity(
            id = id,
            name = name,
            typeOrdinal = type.ordinal,
            requiredPercentage = requiredPercentage,
            geofenceLat = geofenceLat,
            geofenceLng = geofenceLng,
            geofenceRadius = geofenceRadius
        )
    }
}
