package com.classtrack.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance_records",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScheduleSlotEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleSlotId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["subjectId"]), Index(value = ["scheduleSlotId"])]
)
data class AttendanceRecordEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    val scheduleSlotId: String?,
    val timestamp: Long,
    val status: String,
    val isAutoMarked: Boolean,
    val notes: String?
)
