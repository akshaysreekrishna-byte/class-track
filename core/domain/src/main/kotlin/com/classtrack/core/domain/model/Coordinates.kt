package com.classtrack.core.domain.model

/**
 * A plain value object representing a geographic point.
 * No Android imports — safe to use in the Domain layer.
 */
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)
