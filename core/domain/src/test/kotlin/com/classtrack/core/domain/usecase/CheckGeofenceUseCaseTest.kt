package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.model.Coordinates
import com.classtrack.core.domain.model.GeofenceConfig
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CheckGeofenceUseCaseTest {

    private lateinit var useCase: CheckGeofenceUseCase

    // Geofence centred on a real-world coordinate with a 100m radius
    private val testConfig = GeofenceConfig(
        latitude = 28.6139,   // New Delhi, India
        longitude = 77.2090,
        radiusMeters = 100f
    )

    @BeforeEach
    fun setUp() {
        useCase = CheckGeofenceUseCase()
    }

    @Test
    fun given_coordinatesAtExactCenter_when_checked_then_returnsTrue() {
        val coordinates = Coordinates(latitude = 28.6139, longitude = 77.2090)
        assertTrue(useCase(coordinates, testConfig))
    }

    @Test
    fun given_coordinatesJustInsideRadius_when_checked_then_returnsTrue() {
        // ~50m north of the center — well within 100m
        val coordinates = Coordinates(latitude = 28.61435, longitude = 77.2090)
        assertTrue(useCase(coordinates, testConfig))
    }

    @Test
    fun given_coordinatesJustOutsideRadius_when_checked_then_returnsFalse() {
        // ~200m north of the center — outside 100m radius
        val coordinates = Coordinates(latitude = 28.6157, longitude = 77.2090)
        assertFalse(useCase(coordinates, testConfig))
    }

    @Test
    fun given_coordinatesFarAway_when_checked_then_returnsFalse() {
        // Mumbai — roughly 1400 km from New Delhi
        val coordinates = Coordinates(latitude = 19.0760, longitude = 72.8777)
        assertFalse(useCase(coordinates, testConfig))
    }

    @Test
    fun given_antipodalCoordinates_when_checked_then_returnsFalse() {
        // Antipodal point of New Delhi
        val coordinates = Coordinates(latitude = -28.6139, longitude = -102.7910)
        assertFalse(useCase(coordinates, testConfig))
    }

    @Test
    fun given_zeroRadiusConfig_when_coordinatesAtExactCenter_then_returnsTrue() {
        val zeroRadiusConfig = testConfig.copy(radiusMeters = 0f)
        val coordinates = Coordinates(latitude = 28.6139, longitude = 77.2090)
        // distance is ~0m, which is <= 0f
        assertTrue(useCase(coordinates, zeroRadiusConfig))
    }

    @Test
    fun given_zeroRadiusConfig_when_coordinatesOffset_then_returnsFalse() {
        val zeroRadiusConfig = testConfig.copy(radiusMeters = 0f)
        val coordinates = Coordinates(latitude = 28.6140, longitude = 77.2090)
        assertFalse(useCase(coordinates, zeroRadiusConfig))
    }

    @Test
    fun given_largeRadius_when_coordinatesFarAway_then_returnsTrue() {
        // 50km radius should contain a point 1km away
        val largeConfig = testConfig.copy(radiusMeters = 50_000f)
        val coordinates = Coordinates(latitude = 28.6229, longitude = 77.2090) // ~1km north
        assertTrue(useCase(coordinates, largeConfig))
    }
}
