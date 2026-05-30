package com.classtrack.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

// ── Material 3 Light Color Scheme ─────────────────────────────────────────
val Primary = Color(0xFF4F378A)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF6750A4)
val OnPrimaryContainer = Color(0xFFE0D2FF)
val PrimaryFixed = Color(0xFFE9DDFF)
val PrimaryFixedDim = Color(0xFFCFBCFF)
val OnPrimaryFixed = Color(0xFF22005D)
val OnPrimaryFixedVariant = Color(0xFF4F378A)
val InversePrimary = Color(0xFFCFBCFF)

val Secondary = Color(0xFF625B71)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFE8DEF9)
val OnSecondaryContainer = Color(0xFF686177)
val SecondaryFixed = Color(0xFFE8DEF9)
val SecondaryFixedDim = Color(0xFFCCC2DC)
val OnSecondaryFixed = Color(0xFF1E192B)
val OnSecondaryFixedVariant = Color(0xFF4A4358)

val Tertiary = Color(0xFF633B48)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFF7D5260)
val OnTertiaryContainer = Color(0xFFFFCBDA)
val TertiaryFixed = Color(0xFFFFD9E3)
val TertiaryFixedDim = Color(0xFFEEB8C8)
val OnTertiaryFixed = Color(0xFF31111D)
val OnTertiaryFixedVariant = Color(0xFF633B48)

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

val Surface = Color(0xFFFDF8FF)
val SurfaceDim = Color(0xFFDDD8E3)
val SurfaceBright = Color(0xFFFDF8FF)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF7F1FD)
val SurfaceContainer = Color(0xFFF1ECF7)
val SurfaceContainerHigh = Color(0xFFECE6F1)
val SurfaceContainerHighest = Color(0xFFE6E0EB)
val SurfaceVariant = Color(0xFFE6E0EB)
val SurfaceTint = Color(0xFF6750A4)
val OnSurface = Color(0xFF1C1A22)
val OnSurfaceVariant = Color(0xFF494551)
val InverseSurface = Color(0xFF312F37)
val InverseOnSurface = Color(0xFFF4EFFA)

val Background = Color(0xFFFDF8FF)
val OnBackground = Color(0xFF1C1A22)

val Outline = Color(0xFF7A7582)
val OutlineVariant = Color(0xFFCBC4D2)

// ── Semantic Attendance Status Colors ─────────────────────────────────────
val AttendancePresent = Color(0xFF22C55E)
val AttendanceCritical = Color(0xFFBA1A1A)
val AttendanceCancelled = Color(0xFFF59E0B)
val AttendancePending = Color(0xFF9CA3AF)
val AttendanceSafe = Color(0xFF22C55E)

// ── Material 3 Dark Color Scheme ─────────────────────────────────────────
val PrimaryDark = Color(0xFFD0BCFF)
val OnPrimaryDark = Color(0xFF381E72)
val PrimaryContainerDark = Color(0xFF4F378B)
val OnPrimaryContainerDark = Color(0xFFEADDFF)
val SecondaryDark = Color(0xFFCCC2DC)
val OnSecondaryDark = Color(0xFF332D41)
val SecondaryContainerDark = Color(0xFF4A4458)
val OnSecondaryContainerDark = Color(0xFFE8DEF8)
val TertiaryDark = Color(0xFFEFB8C8)
val OnTertiaryDark = Color(0xFF492532)
val TertiaryContainerDark = Color(0xFF633B48)
val OnTertiaryContainerDark = Color(0xFFFFD8E4)
val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)
val BackgroundDark = Color(0xFF141218)
val OnBackgroundDark = Color(0xFFE6E0E9)
val SurfaceDark = Color(0xFF141218) // Gray 900
val OnSurfaceDark = Color(0xFFE6E0E9)
val SurfaceVariantDark = Color(0xFF49454F)
val OnSurfaceVariantDark = Color(0xFFCAC4D0)
val OutlineDark = Color(0xFF938F99)

// ── Semantic Attendance Status Colors CompositionLocal ────────────────────

@Immutable
data class AttendanceStatusColors(
    val present: Color,
    val absent: Color,
    val cancelled: Color,
    val pending: Color
)

val LocalAttendanceColors = staticCompositionLocalOf {
    AttendanceStatusColors(
        present = Color.Unspecified,
        absent = Color.Unspecified,
        cancelled = Color.Unspecified,
        pending = Color.Unspecified
    )
}

