package com.classtrack.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Manages the creation of notification channels and individual notifications
 * for auto-marked attendance events. Kept stateless — all methods are top-level
 * functions on the companion object to avoid needing a Hilt-managed singleton
 * for a purely utility class.
 */
class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(context, NotificationManager::class.java)!!
    }

    fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Auto Attendance",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for automatically marked attendance via geofencing."
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun notifyAutoMarked(subjectName: String, notificationId: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle("Attendance Auto-Marked ✅")
            .setContentText("Marked PRESENT for $subjectName (auto-detected via geofence)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val CHANNEL_ID = "auto_attendance"
    }
}
