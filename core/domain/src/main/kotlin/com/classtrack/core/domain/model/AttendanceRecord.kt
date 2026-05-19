package com.classtrack.core.domain.model

data class AttendanceRecord(
    val id: String,
    val subjectId: String,
    val scheduleSlotId: String? = null,
    val timestamp: Long,
    val status: AttendanceStatus,
    val isAutoMarked: Boolean,
    val notes: String? = null
)
