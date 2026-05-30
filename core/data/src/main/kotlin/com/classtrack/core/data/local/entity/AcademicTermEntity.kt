package com.classtrack.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "academic_terms")
@Serializable
data class AcademicTermEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val isCurrent: Boolean
)
