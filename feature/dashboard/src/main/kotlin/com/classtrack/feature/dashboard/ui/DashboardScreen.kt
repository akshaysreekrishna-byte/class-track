package com.classtrack.feature.dashboard.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp

private val Safe = Color(0xFF22C55E)
private val Critical = Color(0xFFBA1A1A)
private val Cancelled = Color(0xFFF59E0B)
private val Pending = Color(0xFF9CA3AF)
private val CardSurface = Color(0xFFFFFFFF)
private val TrackColor = Color(0xFFCBC4D2)

/**
 * Structural placeholder for the Dashboard destination.
 * Uses inline versions of CircularAttendanceIndicator and status chips
 * until a shared :core:ui module is introduced in a future phase.
 */
@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { AttendanceSummarySection() }
        item { InsightCard() }
        item {
            Text("Subject Overview", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface)
        }
        items(previewSubjects.size) { i -> DashboardSubjectRow(previewSubjects[i]) }
    }
}

@Composable
private fun AttendanceSummarySection() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(192.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val sw = 12.dp.toPx()
                val stroke = Stroke(sw, cap = StrokeCap.Round)
                val rect = Size(size.width - sw, size.height - sw)
                val tl = Offset(sw / 2, sw / 2)
                drawArc(TrackColor, -90f, 360f, false, tl, rect, style = stroke)
                drawArc(Safe, -90f, 360f * 0.78f, false, tl, rect, style = stroke)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("78%", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.SemiBold)
                Text("Total Attendance", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun InsightCard() {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Safe to Skip: Data Structures", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text("Next class won't drop below 75%. You have a buffer session available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun DashboardSubjectRow(data: PreviewSubjectData) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(1.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(data.name, style = MaterialTheme.typography.titleMedium)
                Text(data.statusLabel, style = MaterialTheme.typography.labelSmall,
                    color = data.statusColor, modifier = Modifier.padding(top = 4.dp))
            }
            Text("${data.percentage}%", style = MaterialTheme.typography.headlineMedium,
                color = if (data.percentage >= 75) MaterialTheme.colorScheme.onSurface else Critical)
        }
    }
}

private data class PreviewSubjectData(val name: String, val percentage: Int, val statusLabel: String, val statusColor: Color)
private val previewSubjects = listOf(
    PreviewSubjectData("Calculus", 82, "Safe to Bunk", Safe),
    PreviewSubjectData("Physics", 74, "Critical: Do Not Bunk", Critical),
    PreviewSubjectData("Data Structures", 79, "Safe to Bunk", Safe),
)

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FF)
@Composable
private fun DashboardScreenPreview() {
    MaterialTheme { DashboardScreen() }
}
