package com.attendease.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val typeOrdinal: Int,
    val requiredPercentage: Double,
    val geofenceLat: Double?,
    val geofenceLng: Double?,
    val geofenceRadius: Float?
)
