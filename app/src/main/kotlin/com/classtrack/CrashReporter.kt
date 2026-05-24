package com.classtrack

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CrashReporter {
    fun initialize(context: Context) {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val dir = context.getExternalFilesDir(null)
                if (dir != null) {
                    val file = File(dir, "crash_log.txt")
                    val writer = PrintWriter(FileWriter(file, true))
                    val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    writer.println("---- Crash at $time ----")
                    throwable.printStackTrace(writer)
                    writer.println("--------------------------------------------------")
                    writer.flush()
                    writer.close()
                }
            } catch (e: Exception) {
                // Ignore, we are already crashing
            }
            
            // Call the default handler to let the app crash normally
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
