package com.attendease.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendease.core.domain.usecase.GetAttendanceSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAttendanceSummaryUseCase: GetAttendanceSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAttendanceSummaryUseCase().collect { summaries ->
                val totalClasses = summaries.sumOf { it.totalClasses }
                val presentClasses = summaries.sumOf { it.presentClasses }
                val percentage = if (totalClasses == 0) 0.0 else (presentClasses.toDouble() / totalClasses) * 100.0
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalPercentage = percentage,
                        subjectSummaries = summaries,
                        overallHealthText = if (percentage >= 75.0) "Optimal" else "Critical"
                    )
                }
            }
        }
    }
}
