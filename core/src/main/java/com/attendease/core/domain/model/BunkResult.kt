package com.attendease.core.domain.model

data class BunkResult(
    val currentPercentage: Double,
    val safeBunksRemaining: Int,
    val classesNeededToRecover: Int
)
