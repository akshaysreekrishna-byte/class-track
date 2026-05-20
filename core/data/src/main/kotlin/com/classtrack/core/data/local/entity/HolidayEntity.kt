package com.classtrack.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "holidays",
    foreignKeys = [
        ForeignKey(
            entity = AcademicTermEntity::class,
            parentColumns = ["id"],
            childColumns = ["termId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HolidayEntity(
    @PrimaryKey val id: String,
    val termId: String,
    val name: String,
    val dateTimestamp: Long
)
