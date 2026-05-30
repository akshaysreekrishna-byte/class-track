package com.classtrack.core.data.local.backup

import com.classtrack.core.data.local.entity.*
import kotlinx.serialization.Serializable

@Serializable
data class BackupSchema(
    val version: Int = 1,
    val terms: List<AcademicTermEntity>,
    val subjects: List<SubjectEntity>,
    val scheduleSlots: List<ScheduleSlotEntity>,
    val holidays: List<HolidayEntity>,
    val attendanceRecords: List<AttendanceRecordEntity>
)
