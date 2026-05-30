package com.classtrack.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Encapsulates WorkManager scheduling logic for the geofence check.
 * Calling [schedule] is idempotent — KEEP policy prevents duplicate chains
 * on every app restart.
 */
object WorkManagerScheduler {

    private const val SYNC_WORK_NAME = "schedule_sync_work"

    fun scheduleSync(context: Context) {
        // Run once every 12 hours
        val request = PeriodicWorkRequestBuilder<ScheduleSyncWorker>(
            12, TimeUnit.HOURS
        )
            .addTag(SYNC_WORK_NAME)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun scheduleOneTimeGeofenceCheck(context: Context, slotId: String, subjectId: String, delayMinutes: Long) {
        val inputData = Data.Builder()
            .putString("SLOT_ID", slotId)
            .putString("SUBJECT_ID", subjectId)
            .build()

        val request = OneTimeWorkRequestBuilder<GeofenceCheckWorker>()
            .setInputData(inputData)
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .addTag("geofence_check_$slotId")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "geofence_check_${slotId}_${java.time.LocalDate.now()}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }
}
