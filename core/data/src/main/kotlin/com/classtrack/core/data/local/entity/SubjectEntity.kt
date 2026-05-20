package com.classtrack.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subjects",
    foreignKeys = [
        ForeignKey(
            entity = AcademicTermEntity::class,
            parentColumns = ["id"],
            childColumns = ["termId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["termId"])]
)
data class SubjectEntity(
    @PrimaryKey
    val id: String,
    val termId: String,
    val name: String,
    val type: String,
    val minAttendancePercentage: Float,
    @Embedded(prefix = "geo_")
    val geofenceConfig: GeofenceConfigEntity? = null
)

data class GeofenceConfigEntity(
    val latitude: Double?,
    val longitude: Double?,
    val radiusMeters: Float?
)
