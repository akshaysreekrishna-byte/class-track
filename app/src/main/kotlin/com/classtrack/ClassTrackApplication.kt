package com.classtrack

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.classtrack.notification.NotificationHelper
import com.classtrack.worker.WorkManagerScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application entry point.
 * Implements [Configuration.Provider] to supply [HiltWorkerFactory] to WorkManager,
 * enabling constructor injection (@AssistedInject) in [GeofenceCheckWorker].
 */
@HiltAndroidApp
class ClassTrackApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        CrashReporter.initialize(this)
        notificationHelper.createChannel()
        // Start background scheduling
        WorkManagerScheduler.scheduleSync(this)
    }
}
