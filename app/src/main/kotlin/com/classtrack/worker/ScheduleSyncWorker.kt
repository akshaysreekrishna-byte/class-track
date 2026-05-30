package com.classtrack.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.classtrack.core.domain.usecase.GetGeofenceScheduleForDayUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

@HiltWorker
class ScheduleSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getGeofenceScheduleForDayUseCase: GetGeofenceScheduleForDayUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val todayCalendar = Calendar.getInstance()
        val todayDayOfWeek = todayCalendar.get(Calendar.DAY_OF_WEEK)
        val todayDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // We check today's schedule and pre-register OneTimeWorkRequests
        val activeSlots = getGeofenceScheduleForDayUseCase(todayDayOfWeek)
            .firstOrNull()
            ?: return Result.success()

        activeSlots.forEach { (slot, subject) ->
            if (subject.geofenceConfig != null) {
                try {
                    val startTime = LocalTime.parse(slot.startTime, timeFormatter)
                    
                    // We want to trigger it right when the class starts
                    // If it hasn't started yet, schedule it.
                    if (currentTime.isBefore(startTime)) {
                        val delayMinutes = ChronoUnit.MINUTES.between(currentTime, startTime)
                        WorkManagerScheduler.scheduleOneTimeGeofenceCheck(
                            context = applicationContext,
                            slotId = slot.id,
                            subjectId = subject.id,
                            delayMinutes = delayMinutes
                        )
                    } else {
                        // If it's already active, we might want to check it immediately if we haven't already.
                        val endTime = LocalTime.parse(slot.endTime, timeFormatter)
                        if (currentTime.isBefore(endTime)) {
                            WorkManagerScheduler.scheduleOneTimeGeofenceCheck(
                                context = applicationContext,
                                slotId = slot.id,
                                subjectId = subject.id,
                                delayMinutes = 0
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Ignore parse errors
                }
            }
        }

        return Result.success()
    }
}
