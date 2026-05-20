package com.classtrack.core.data.mapper

import com.classtrack.core.data.local.entity.GeofenceConfigEntity
import com.classtrack.core.data.local.entity.SubjectEntity
import com.classtrack.core.domain.model.GeofenceConfig
import com.classtrack.core.domain.model.Subject
import com.classtrack.core.domain.model.SubjectType

fun SubjectEntity.toDomain(): Subject {
    val domainConfig = if (this.geofenceConfig?.latitude != null && this.geofenceConfig.longitude != null && this.geofenceConfig.radiusMeters != null) {
        GeofenceConfig(
            latitude = this.geofenceConfig.latitude,
            longitude = this.geofenceConfig.longitude,
            radiusMeters = this.geofenceConfig.radiusMeters
        )
    } else null

    return Subject(
        id = this.id,
        termId = this.termId,
        name = this.name,
        type = try { SubjectType.valueOf(this.type) } catch (e: Exception) { SubjectType.THEORY },
        minAttendancePercentage = this.minAttendancePercentage,
        geofenceConfig = domainConfig
    )
}

fun Subject.toEntity(): SubjectEntity {
    val entityConfig = this.geofenceConfig?.let {
        GeofenceConfigEntity(
            latitude = it.latitude,
            longitude = it.longitude,
            radiusMeters = it.radiusMeters
        )
    }

    return SubjectEntity(
        id = this.id,
        termId = this.termId,
        name = this.name,
        type = this.type.name,
        minAttendancePercentage = this.minAttendancePercentage,
        geofenceConfig = entityConfig
    )
}
