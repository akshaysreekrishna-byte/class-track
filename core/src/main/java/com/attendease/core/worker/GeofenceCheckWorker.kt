package com.attendease.core.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.attendease.core.domain.model.AttendanceRecord
import com.attendease.core.domain.model.AttendanceStatus
import com.attendease.core.domain.repository.AttendanceRepository
import com.attendease.core.domain.repository.SubjectRepository
import com.attendease.core.domain.usecase.MarkAttendanceUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import java.time.LocalDate

@HiltWorker
class GeofenceCheckWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val subjectRepository: SubjectRepository,
    private val attendanceRepository: AttendanceRepository,
    private val markAttendanceUseCase: MarkAttendanceUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (!hasLocationPermissions()) {
            Timber.w("Location permissions missing, silently failing geofence check")
            return Result.success() // Fail silently without aggressive retry
        }

        val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        
        val lastKnownLocation: Location? = try {
            val providers = locationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val l = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
            bestLocation
        } catch (e: SecurityException) {
            Timber.e(e, "SecurityException while fetching location")
            null
        }

        if (lastKnownLocation == null) {
            Timber.w("Location unavailable, not retrying aggressively")
            return Result.success()
        }

        val subjectIdToVerify = inputData.getLong("verify_subject_id", -1L)

        if (subjectIdToVerify != -1L) {
            // VERIFICATION PHASE: 5 minutes have passed since first sighting
            val subjectResult = subjectRepository.getSubjectById(subjectIdToVerify).firstOrNull()?.getOrNull()
            if (subjectResult != null) {
                val lat = subjectResult.geofenceLat
                val lng = subjectResult.geofenceLng
                val radius = subjectResult.geofenceRadius ?: 100f

                if (lat != null && lng != null) {
                    val results = FloatArray(1)
                    Location.distanceBetween(lastKnownLocation.latitude, lastKnownLocation.longitude, lat, lng, results)
                    val distanceInMeters = results[0]

                    if (distanceInMeters <= radius) {
                        markAttendanceAndNotify(subjectResult)
                    }
                }
            }
        } else {
            // PERIODIC PHASE: Check all subjects and schedule verification if inside geofence
            val subjectsResult = subjectRepository.getAllSubjects().firstOrNull()?.getOrNull() ?: emptyList()

            for (subject in subjectsResult) {
                val lat = subject.geofenceLat ?: continue
                val lng = subject.geofenceLng ?: continue
                val radius = subject.geofenceRadius ?: 100f

                val results = FloatArray(1)
                Location.distanceBetween(lastKnownLocation.latitude, lastKnownLocation.longitude, lat, lng, results)
                val distanceInMeters = results[0]

                if (distanceInMeters <= radius) {
                    // Check Idempotency: Has attendance already been marked today?
                    val today = LocalDate.now()
                    val recordsResult = attendanceRepository.getRecordsForSubject(subject.id).firstOrNull()?.getOrNull() ?: emptyList()
                    val alreadyMarked = recordsResult.any { it.date == today }

                    if (!alreadyMarked) {
                        // User is within geofence, schedule 5-minute verification
                        val data = Data.Builder()
                            .putLong("verify_subject_id", subject.id)
                            .build()
                            
                        val verifyRequest = OneTimeWorkRequestBuilder<GeofenceCheckWorker>()
                            .setInitialDelay(5, java.util.concurrent.TimeUnit.MINUTES)
                            .setInputData(data)
                            .build()
                            
                        WorkManager.getInstance(appContext).enqueue(verifyRequest)
                    }
                }
            }
        }

        return Result.success()
    }

    private suspend fun markAttendanceAndNotify(subject: com.attendease.core.domain.model.Subject) {
        val today = LocalDate.now()
        val recordsResult = attendanceRepository.getRecordsForSubject(subject.id).firstOrNull()?.getOrNull() ?: emptyList()
        val alreadyMarked = recordsResult.any { it.date == today }

        if (!alreadyMarked) {
            val newRecord = AttendanceRecord(
                subjectId = subject.id,
                date = today,
                status = AttendanceStatus.PRESENT, // NEVER automatic ABSENT
                isManualOverride = false
            )
            val result = markAttendanceUseCase(newRecord)
            if (result.isSuccess) {
                sendNotification(subject.name)
            }
        }
    }

    private fun hasLocationPermissions(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocation == PackageManager.PERMISSION_GRANTED || coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun sendNotification(subjectName: String) {
        val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "geofence_attendance_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Attendance Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for automatic attendance marking"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Fallback icon
            .setContentTitle("Attendance Marked")
            .setContentText("Attendance marked for $subjectName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // Notification ID based on subjectName hash ensures we can post multiple if needed, or update the same one
        notificationManager.notify(subjectName.hashCode(), notification)
    }

    companion object {
        fun startPeriodicWork(context: Context) {
            val constraints = androidx.work.Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .build()

            val request = androidx.work.PeriodicWorkRequestBuilder<GeofenceCheckWorker>(
                15, java.util.concurrent.TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "GeofenceCheckWorker",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
