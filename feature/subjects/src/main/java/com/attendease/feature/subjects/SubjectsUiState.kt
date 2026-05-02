package com.attendease.feature.subjects

import com.attendease.core.domain.model.Subject

data class SubjectsUiState(
    val isLoading: Boolean = false,
    val subjects: List<Subject> = emptyList(),
    val error: String? = null
)
