package com.attendease.core.domain.model

data class Subject(
    val id: Long = 0,
    val name: String,
    val type: SubjectType,
    val requiredPercentage: Double,
    val geofenceLat: Double? = null,
    val geofenceLng: Double? = null,
    val geofenceRadius: Float? = null
)
