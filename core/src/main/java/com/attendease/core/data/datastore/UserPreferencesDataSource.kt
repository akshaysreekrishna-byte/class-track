package com.attendease.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesDataSource(
    private val context: Context
) {
    companion object {
        val REQUIRED_ATTENDANCE_PERCENTAGE = doublePreferencesKey("required_attendance_percentage")
    }

    val requiredAttendancePercentage: Flow<Double> = context.dataStore.data.map { preferences ->
        preferences[REQUIRED_ATTENDANCE_PERCENTAGE] ?: 75.0
    }

    suspend fun setRequiredAttendancePercentage(percentage: Double) {
        context.dataStore.edit { preferences ->
            preferences[REQUIRED_ATTENDANCE_PERCENTAGE] = percentage
        }
    }
}
