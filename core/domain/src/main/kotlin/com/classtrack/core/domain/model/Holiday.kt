package com.classtrack.core.domain.model

import java.util.UUID

data class Holiday(
    val id: String = UUID.randomUUID().toString(),
    val termId: String,
    val name: String,
    val dateTimestamp: Long
)
