package com.classtrack.core.domain.location

import com.classtrack.core.domain.model.Coordinates
import com.classtrack.core.domain.model.Outcome

/**
 * Domain-layer abstraction for retrieving the device's current location.
 * Lives in Domain so Use Cases can depend on it without touching Android.
 * The implementation (AospLocationProvider) lives in core:data.
 */
interface LocationProvider {
    suspend fun getCurrentLocation(): Outcome<Coordinates>
}
