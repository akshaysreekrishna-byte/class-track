package com.classtrack.core.data.mapper

import com.classtrack.core.data.local.entity.AcademicTermEntity
import com.classtrack.core.domain.model.AcademicTerm

fun AcademicTermEntity.toDomain(): AcademicTerm {
    return AcademicTerm(
        id = this.id,
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate,
        isCurrent = this.isCurrent
    )
}

fun AcademicTerm.toEntity(): AcademicTermEntity {
    return AcademicTermEntity(
        id = this.id,
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate,
        isCurrent = this.isCurrent
    )
}
