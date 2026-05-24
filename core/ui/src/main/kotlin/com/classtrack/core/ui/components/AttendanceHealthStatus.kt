package com.classtrack.core.ui.components

import androidx.compose.ui.graphics.Color
import com.classtrack.core.ui.theme.AttendanceCancelled
import com.classtrack.core.ui.theme.AttendanceCritical
import com.classtrack.core.ui.theme.AttendancePending
import com.classtrack.core.ui.theme.AttendanceSafe

/**
 * Semantic attendance health state used across all feature modules.
 * Drives the 4-color status system throughout the app.
 *
 *  SAFE      → Green  — attendance above target; can skip classes
 *  CRITICAL  → Red    — attendance below target; must attend
 *  CANCELLED → Amber  — class was cancelled by instructor
 *  PENDING   → Grey   — upcoming or unrecorded class slot
 */
enum class AttendanceHealthStatus {
    SAFE, CRITICAL, CANCELLED, PENDING;

    fun toColor(): Color = when (this) {
        SAFE -> AttendanceSafe
        CRITICAL -> AttendanceCritical
        CANCELLED -> AttendanceCancelled
        PENDING -> AttendancePending
    }

    fun toLabel(): String = when (this) {
        SAFE -> "Safe to Bunk"
        CRITICAL -> "Critical: Do Not Bunk"
        CANCELLED -> "Cancelled"
        PENDING -> "Pending"
    }
}
