package com.classtrack.core.domain.model

data class AcademicTerm(
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val isCurrent: Boolean
)
