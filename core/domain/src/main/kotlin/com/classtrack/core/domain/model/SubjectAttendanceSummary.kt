package com.classtrack.core.domain.model

data class SubjectAttendanceSummary(
    val subject: Subject,
    val totalClasses: Int,
    val presentCount: Int,
    val currentPercentage: Float,
    val statusText: String,
    val actionCount: Int
)
