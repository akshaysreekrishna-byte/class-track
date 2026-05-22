package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.model.Coordinates
import com.classtrack.core.domain.model.GeofenceConfig
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Determines whether a coordinate is within a configured geofence radius.
 *
 * Uses the Haversine formula for accurate great-circle distance computation:
 *   d = 2R · arcsin( sqrt( sin²(Δφ/2) + cos(φ₁)·cos(φ₂)·sin²(Δλ/2) ) )
 *
 * Pure Kotlin — zero Android imports. JVM unit-testable.
 */
class CheckGeofenceUseCase {

    operator fun invoke(coordinates: Coordinates, config: GeofenceConfig): Boolean {
        val distanceMeters = haversineDistance(
            lat1 = coordinates.latitude,
            lon1 = coordinates.longitude,
            lat2 = config.latitude,
            lon2 = config.longitude
        )
        return distanceMeters <= config.radiusMeters
    }

    private fun haversineDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadiusMeters = 6_371_000.0

        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = sin(deltaPhi / 2).pow(2) +
            cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2)

        val c = 2 * asin(sqrt(a))

        return earthRadiusMeters * c
    }
}
