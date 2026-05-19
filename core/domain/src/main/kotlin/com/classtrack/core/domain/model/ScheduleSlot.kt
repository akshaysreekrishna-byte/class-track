package com.classtrack.core.domain.model

data class ScheduleSlot(
    val id: String,
    val subjectId: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val roomNumber: String? = null
)
