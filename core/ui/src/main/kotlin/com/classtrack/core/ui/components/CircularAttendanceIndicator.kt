package com.classtrack.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.classtrack.core.ui.theme.OutlineVariant

/**
 * Circular arc progress indicator showing an attendance percentage.
 *
 * The stroke color is driven by the caller — use [AttendanceHealthStatus.toColor()]
 * to apply the semantic 4-color system. Drawn entirely with Canvas; zero third-party deps.
 *
 * @param percentage   Current attendance percentage (0–100).
 * @param arcColor     The semantic color for the progress arc.
 * @param label        Text shown below the percentage inside the ring.
 * @param size         Diameter of the ring in dp. Defaults to 192.dp.
 * @param strokeWidth  Ring stroke thickness. Defaults to 12.dp.
 */
@Composable
fun CircularAttendanceIndicator(
    percentage: Float,
    arcColor: Color,
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 192.dp,
    strokeWidth: Dp = 12.dp,
) {
    val sweepAngle = remember(percentage) { 360f * (percentage.coerceIn(0f, 100f) / 100f) }

    Box(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = "${percentage.toInt()}% $label" },
        contentAlignment = Alignment.Center,
    ) {
        AttendanceArc(
            sweepAngle = sweepAngle,
            arcColor = arcColor,
            strokeWidth = strokeWidth,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${percentage.toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AttendanceArc(
    sweepAngle: Float,
    arcColor: Color,
    strokeWidth: Dp,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val inset = strokeWidth.toPx() / 2f
        val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
        val topLeft = Offset(inset, inset)
        // Background track
        drawArc(OutlineVariant, -90f, 360f, false, topLeft, arcSize, style = stroke)
        // Progress arc
        drawArc(arcColor, -90f, sweepAngle, false, topLeft, arcSize, style = stroke)
    }
}
