package com.classtrack.core.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.classtrack.core.domain.location.LocationProvider
import com.classtrack.core.domain.model.Coordinates
import com.classtrack.core.domain.model.Outcome
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * AOSP-compliant implementation of [LocationProvider].
 * Uses [LocationManager] exclusively — no Google Play Services dependency.
 *
 * Strategy:
 *  1. Try GPS_PROVIDER with a 15-second coroutine timeout (handles deep-indoor failures).
 *  2. On GPS timeout, fall back to NETWORK_PROVIDER with the same 15-second window.
 *  3. If both fail, attempt getLastKnownLocation as a final fallback.
 *  4. Return Outcome.Error if all strategies are exhausted or permission is absent.
 */
class AospLocationProvider(private val context: Context) : LocationProvider {

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override suspend fun getCurrentLocation(): Outcome<Coordinates> {
        if (!hasLocationPermission()) {
            return Outcome.Error("Location permission not granted.")
        }

        return requestFromProvider(LocationManager.GPS_PROVIDER)
            ?: requestFromProvider(LocationManager.NETWORK_PROVIDER)
            ?: getLastKnownFallback()
            ?: Outcome.Error("Unable to determine location from any provider.")
    }

    private suspend fun requestFromProvider(provider: String): Outcome<Coordinates>? {
        if (!locationManager.isProviderEnabled(provider)) return null

        return withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        locationManager.removeUpdates(this)
                        if (continuation.isActive) {
                            continuation.resume(
                                Outcome.Success(location.toCoordinates())
                            )
                        }
                    }

                    @Deprecated("Deprecated in API 29")
                    override fun onStatusChanged(p: String?, s: Int, e: Bundle?) = Unit
                }

                try {
                    @Suppress("MissingPermission")
                    locationManager.requestLocationUpdates(
                        provider,
                        MIN_INTERVAL_MS,
                        MIN_DISTANCE_METERS,
                        listener
                    )
                    continuation.invokeOnCancellation {
                        locationManager.removeUpdates(listener)
                    }
                } catch (e: Exception) {
                    continuation.resume(Outcome.Error("Provider $provider failed: ${e.message}", e))
                }
            }
        }
    }

    private fun getLastKnownFallback(): Outcome<Coordinates>? {
        if (!hasLocationPermission()) return null
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        return providers.firstNotNullOfOrNull { provider ->
            @Suppress("MissingPermission")
            locationManager.getLastKnownLocation(provider)
        }?.let { Outcome.Success(it.toCoordinates()) }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun Location.toCoordinates() = Coordinates(latitude = latitude, longitude = longitude)

    private companion object {
        const val LOCATION_TIMEOUT_MS = 15_000L
        const val MIN_INTERVAL_MS = 0L
        const val MIN_DISTANCE_METERS = 0f
    }
}
