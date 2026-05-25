package com.classtrack.core.domain.model

data class Subject(
    val id: String,
    val termId: String,
    val name: String,
    val type: SubjectType,
    val minAttendancePercentage: Float,
    val geofenceConfig: GeofenceConfig? = null
)
