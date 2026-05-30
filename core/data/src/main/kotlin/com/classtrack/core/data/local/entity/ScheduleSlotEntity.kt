package com.classtrack.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "schedule_slots",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"])]
)
@Serializable
data class ScheduleSlotEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val roomNumber: String?
)
