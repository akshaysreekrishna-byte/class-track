package com.attendease.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendease.core.domain.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadRecordsForDate(_uiState.value.selectedDate)
    }

    fun onDateSelected(date: LocalDate) {
        if (date.isAfter(LocalDate.now())) return // Future dates non-interactive
        
        _uiState.update { it.copy(selectedDate = date) }
        loadRecordsForDate(date)
    }

    private fun loadRecordsForDate(date: LocalDate) {
        viewModelScope.launch {
            attendanceRepository.getRecordsForDate(date).collect { result ->
                val records = result.getOrDefault(emptyList())
                _uiState.update { it.copy(recordsForDate = records) }
            }
        }
    }
}
