package com.attendease.core.domain.model

data class AttendanceSummary(
    val subject: Subject,
    val totalClasses: Int,
    val presentClasses: Int,
    val absentClasses: Int,
    val currentPercentage: Double
)
