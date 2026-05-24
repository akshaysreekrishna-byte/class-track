package com.classtrack.feature.dashboard.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.classtrack.core.ui.components.AttendanceHealthStatus
import com.classtrack.core.ui.components.CircularAttendanceIndicator

/**
 * Hero KPI section: animated circular arc showing the overall attendance %.
 * The sweep angle animates from 0 → target on first composition (600ms ease).
 */
@Composable
fun OverallKpiSection(
    percentage: Float,
    healthStatus: AttendanceHealthStatus,
    modifier: Modifier = Modifier,
) {
    var animTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(percentage) { animTarget = percentage }
    val animatedPct by animateFloatAsState(
        targetValue = animTarget,
        animationSpec = tween(durationMillis = 600),
        label = "kpi_arc",
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularAttendanceIndicator(
            percentage = animatedPct,
            arcColor = healthStatus.toColor(),
            label = "Total Attendance",
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}
