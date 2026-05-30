package com.classtrack.core.data.local.backup

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.classtrack.core.data.local.ClassTrackDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: ClassTrackDatabase
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun exportData(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val schema = database.withTransaction {
                BackupSchema(
                    terms = database.backupDao.getAllTerms(),
                    subjects = database.backupDao.getAllSubjects(),
                    scheduleSlots = database.backupDao.getAllScheduleSlots(),
                    holidays = database.backupDao.getAllHolidays(),
                    attendanceRecords = database.backupDao.getAllAttendanceRecords()
                )
            }

            val jsonString = json.encodeToString(schema)
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            } ?: throw Exception("Unable to open output stream for URI")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importData(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.bufferedReader().readText()
            } ?: throw Exception("Unable to open input stream for URI")

            val schema = json.decodeFromString<BackupSchema>(jsonString)

            database.withTransaction {
                // Delete in leaf-to-root order
                database.backupDao.clearAttendanceRecords()
                database.backupDao.clearScheduleSlots()
                database.backupDao.clearSubjects()
                database.backupDao.clearHolidays()
                database.backupDao.clearTerms()

                // Insert in root-to-leaf order
                database.backupDao.insertTerms(schema.terms)
                database.backupDao.insertHolidays(schema.holidays)
                database.backupDao.insertSubjects(schema.subjects)
                database.backupDao.insertScheduleSlots(schema.scheduleSlots)
                database.backupDao.insertAttendanceRecords(schema.attendanceRecords)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
