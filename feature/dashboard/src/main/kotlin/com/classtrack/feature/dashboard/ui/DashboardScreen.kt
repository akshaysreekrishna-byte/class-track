package com.classtrack.feature.dashboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.classtrack.feature.dashboard.ui.components.DashboardEmptyState
import com.classtrack.feature.dashboard.ui.components.InsightBannerCard
import com.classtrack.feature.dashboard.ui.components.OverallKpiSection
import com.classtrack.feature.dashboard.ui.components.SubjectSummaryCard
import com.classtrack.feature.dashboard.ui.state.DashboardUiState

/**
 * Stateless root of the Dashboard tab.
 * All state is collected via [DashboardViewModel]; composable is a pure render function.
 */
@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardContent(state = uiState, modifier = modifier)
}

@Composable
private fun DashboardContent(
    state: DashboardUiState,
    modifier: Modifier = Modifier,
) {
    when (state) {
        DashboardUiState.Loading -> LoadingIndicator(modifier)
        DashboardUiState.NoSemester -> DashboardEmptyState(
            title = "No Semester Found",
            subtitle = "Head to the Subjects tab to create your first semester and start tracking.",
            modifier = modifier,
        )
        DashboardUiState.Empty -> DashboardEmptyState(
            title = "No Subjects Enrolled",
            subtitle = "Add subjects in the Subjects tab to see your attendance dashboard.",
            modifier = modifier,
        )
        DashboardUiState.NoData -> DashboardEmptyState(
            title = "No Attendance Recorded Yet",
            subtitle = "Start marking attendance in the Calendar view to see your statistics here.",
            modifier = modifier,
        )
        is DashboardUiState.Success -> SuccessDashboard(state = state, modifier = modifier)
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun SuccessDashboard(
    state: DashboardUiState.Success,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(key = "kpi") {
            OverallKpiSection(
                percentage = state.overallPercentage,
                healthStatus = state.overallHealthStatus,
            )
        }
        state.topInsight?.let { insight ->
            item(key = "insight") { InsightBannerCard(insight = insight) }
        }
        item(key = "header") {
            SubjectOverviewHeader(modifier = Modifier.fillMaxWidth())
        }
        items(items = state.subjects, key = { it.id }) { subject ->
            SubjectSummaryCard(subject = subject)
        }
    }
}

@Composable
private fun SubjectOverviewHeader(modifier: Modifier = Modifier) {
    Text(
        text = "Subject Overview",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}
