package com.classtrack.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.classtrack.ui.theme.AttendanceCritical
import com.classtrack.ui.theme.AttendanceSafe
import com.classtrack.ui.theme.ClassTrackTheme
import com.classtrack.ui.theme.OutlineVariant

/**
 * Circular arc progress indicator showing attendance percentage.
 * Stroke color is determined by semantic thresholds:
 *   ≥75% → safe green · <75% → critical red
 *
 * Drawn via Canvas so there is zero dependency on any third-party chart library.
 */
@Composable
fun CircularAttendanceIndicator(
    percentage: Float,
    modifier: Modifier = Modifier,
    size: Dp = 192.dp,
    strokeWidth: Dp = 12.dp,
) {
    val strokeColor = if (percentage >= 75f) AttendanceSafe else AttendanceCritical
    val trackColor = OutlineVariant

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        AttendanceArc(
            percentage = percentage,
            strokeColor = strokeColor,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
        )
        AttendanceCenterLabel(percentage = percentage)
    }
}

@Composable
private fun androidx.compose.foundation.layout.BoxScope.AttendanceArc(
    percentage: Float,
    strokeColor: Color,
    trackColor: Color,
    strokeWidth: Dp,
) {
    Canvas(modifier = Modifier.matchParentSize()) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val inset = strokeWidth.toPx() / 2f
        val arcRect = Size(size.width - inset * 2, size.height - inset * 2)
        val topLeft = Offset(inset, inset)

        // Background track
        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcRect,
            style = stroke,
        )
        // Progress arc
        drawArc(
            color = strokeColor,
            startAngle = -90f,
            sweepAngle = 360f * (percentage / 100f),
            useCenter = false,
            topLeft = topLeft,
            size = arcRect,
            style = stroke,
        )
    }
}

@Composable
private fun AttendanceCenterLabel(percentage: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "Total Attendance",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FF)
@Composable
private fun CircularAttendanceIndicatorPreview() {
    ClassTrackTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularAttendanceIndicator(percentage = 78f)
            CircularAttendanceIndicator(percentage = 74f)
        }
    }
}
