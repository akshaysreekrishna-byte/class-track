package com.classtrack.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.classtrack.core.domain.location.LocationProvider
import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.model.AttendanceStatus
import com.classtrack.core.domain.model.Outcome
import com.classtrack.core.domain.usecase.CheckGeofenceUseCase
import com.classtrack.core.domain.usecase.GetGeofenceScheduleForDayUseCase
import com.classtrack.core.domain.usecase.MarkAttendanceUseCase
import com.classtrack.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Periodic background worker that performs geofence-based auto-attendance.
 *
 * Execution flow per run:
 * 1. Determine today's dayOfWeek.
 * 2. Fetch schedule slots with their subjects for today.
 * 3. Filter for slots whose time window is currently active.
 * 4. For each active slot with a geofenceConfig on its subject:
 *    a. Request current AOSP location.
 *    b. Run CheckGeofenceUseCase.
 *    c. If inside geofence → call MarkAttendanceUseCase with isAutoMarked=true.
 *    d. Fire a local notification.
 *
 * Duplicate Prevention: The AttendanceRecord id is deterministic:
 *   "${slotId}_${localDate}" — so if WorkManager fires again within the same
 *   class session, MarkAttendanceUseCase's collision guard silently ignores it.
 */
@HiltWorker
class GeofenceCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getGeofenceScheduleForDayUseCase: GetGeofenceScheduleForDayUseCase,
    private val checkGeofenceUseCase: CheckGeofenceUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase,
    private val locationProvider: LocationProvider,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val todayDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val todayDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val activeSlots = getGeofenceScheduleForDayUseCase(todayDayOfWeek)
            .firstOrNull()
            ?: return Result.success()

        val eligibleSlots = activeSlots.filter { (slot, subject) ->
            subject.geofenceConfig != null && isSlotActive(slot.startTime, slot.endTime, currentTime, timeFormatter)
        }

        if (eligibleSlots.isEmpty()) return Result.success()

        val locationOutcome = locationProvider.getCurrentLocation()
        if (locationOutcome is Outcome.Error) return Result.success()

        val coordinates = (locationOutcome as Outcome.Success).data

        eligibleSlots.forEachIndexed { index, (slot, subject) ->
            val config = subject.geofenceConfig ?: return@forEachIndexed
            val isInsideGeofence = checkGeofenceUseCase(coordinates, config)

            if (isInsideGeofence) {
                val record = AttendanceRecord(
                    id = "${slot.id}_${todayDate}",
                    subjectId = subject.id,
                    scheduleSlotId = slot.id,
                    timestamp = System.currentTimeMillis(),
                    status = AttendanceStatus.PRESENT,
                    isAutoMarked = true
                )
                markAttendanceUseCase(record)
                notificationHelper.notifyAutoMarked(
                    subjectName = subject.name,
                    notificationId = slot.id.hashCode() + todayDate.hashCode()
                )
            }
        }

        return Result.success()
    }

    private fun isSlotActive(
        startTime: String,
        endTime: String,
        currentTime: LocalTime,
        formatter: DateTimeFormatter
    ): Boolean {
        return try {
            val start = LocalTime.parse(startTime, formatter)
            val end = LocalTime.parse(endTime, formatter)
            !currentTime.isBefore(start) && !currentTime.isAfter(end)
        } catch (e: Exception) {
            false
        }
    }
}
