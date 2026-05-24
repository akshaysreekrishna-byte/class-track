package com.classtrack.feature.subjects.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.classtrack.core.ui.theme.AttendanceCancelled
import com.classtrack.core.ui.theme.AttendanceCritical
import com.classtrack.core.ui.theme.AttendanceSafe
import com.classtrack.core.ui.theme.OutlineVariant

/**
 * Horizontal stacked bar chart showing Present / Absent breakdown.
 * Drawn entirely with Canvas — no third-party chart library.
 *
 * The bar segments are keyed by [presentCount]/[totalCount] so Canvas
 * only redraws when the underlying data changes.
 *
 * @param presentCount   Number of classes attended.
 * @param totalCount     Total classes held.
 * @param modifier       Standard Compose modifier.
 */
@Composable
fun SubjectAnalyticsBar(
    presentCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
) {
    val presentFraction = remember(presentCount, totalCount) {
        if (totalCount == 0) 0f else presentCount.toFloat() / totalCount.toFloat()
    }
    val absentFraction = remember(presentCount, totalCount) {
        if (totalCount == 0) 0f else 1f - presentFraction
    }
    val a11yLabel = "$presentCount present out of $totalCount total classes"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
            .semantics { contentDescription = a11yLabel },
    ) {
        StackedBarCanvas(
            presentFraction = presentFraction,
            absentFraction = absentFraction,
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        ) {
            Text(
                text = "✓ $presentCount Present",
                style = MaterialTheme.typography.labelSmall,
                color = AttendanceSafe,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$totalCount Total",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StackedBarCanvas(
    presentFraction: Float,
    absentFraction: Float,
) {
    Canvas(
        modifier = Modifier.fillMaxWidth().height(8.dp),
    ) {
        val barHeight = size.height
        val totalWidth = size.width
        val presentWidth = totalWidth * presentFraction
        val absentWidth = totalWidth * absentFraction

        // Background track
        drawRect(color = OutlineVariant, size = Size(totalWidth, barHeight))
        // Present segment (green)
        if (presentWidth > 0f) {
            drawRect(color = AttendanceSafe, size = Size(presentWidth, barHeight))
        }
        // Absent segment (red) — drawn right of present
        if (absentWidth > 0f) {
            drawRect(
                color = AttendanceCritical.copy(alpha = 0.7f),
                topLeft = androidx.compose.ui.geometry.Offset(presentWidth, 0f),
                size = Size(absentWidth, barHeight),
            )
        }
    }
}
