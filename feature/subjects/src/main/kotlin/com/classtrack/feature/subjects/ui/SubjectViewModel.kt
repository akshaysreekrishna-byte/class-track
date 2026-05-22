package com.classtrack.feature.subjects.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classtrack.core.domain.model.AcademicTerm
import com.classtrack.core.domain.model.Subject
import com.classtrack.core.domain.model.SubjectType
import com.classtrack.core.domain.repository.AcademicTermRepository
import com.classtrack.core.domain.repository.SubjectRepository
import com.classtrack.core.domain.usecase.GetSubjectsForCurrentTermUseCase
import com.classtrack.feature.subjects.ui.state.SubjectAccentColor
import com.classtrack.feature.subjects.ui.state.SubjectDialogState
import com.classtrack.feature.subjects.ui.state.SubjectUiItem
import com.classtrack.feature.subjects.ui.state.SubjectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsForCurrentTermUseCase: GetSubjectsForCurrentTermUseCase,
    private val subjectRepository: SubjectRepository,
    private val termRepository: AcademicTermRepository,
) : ViewModel() {

    private val _dialogState = MutableStateFlow<SubjectDialogState>(SubjectDialogState.Hidden)
    val dialogState: StateFlow<SubjectDialogState> = _dialogState.asStateFlow()

    val uiState: StateFlow<SubjectUiState> = combine(
        termRepository.getCurrentTerm(),
        getSubjectsForCurrentTermUseCase(),
    ) { term, subjects ->
        buildUiState(term, subjects)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SubjectUiState.Loading,
    )

    val allTerms: StateFlow<List<AcademicTerm>> = termRepository.getAllTerms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val currentTerm: StateFlow<AcademicTerm?> = termRepository.getCurrentTerm()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // ── Dialog event handlers ─────────────────────────────────────────────

    fun onAddSubjectClick() { _dialogState.value = SubjectDialogState.AddNew }

    fun onEditSubjectClick(subject: SubjectUiItem) {
        _dialogState.value = SubjectDialogState.Edit(subject)
    }

    fun onDeleteSubjectClick(subjectId: String, name: String) {
        _dialogState.value = SubjectDialogState.ConfirmDelete(subjectId, name)
    }

    fun onCreateSemesterClick() { _dialogState.value = SubjectDialogState.CreateSemester }

    fun onDismissDialog() { _dialogState.value = SubjectDialogState.Hidden }

    // ── Confirmed actions ─────────────────────────────────────────────────

    fun onConfirmAddSubject(name: String, type: SubjectType, minPct: Float) {
        val termId = currentTerm.value?.id ?: return
        viewModelScope.launch {
            subjectRepository.insertSubject(
                Subject(
                    id = UUID.randomUUID().toString(),
                    termId = termId,
                    name = name.trim(),
                    type = type,
                    minAttendancePercentage = minPct,
                )
            )
            _dialogState.value = SubjectDialogState.Hidden
        }
    }

    fun onConfirmEditSubject(updated: SubjectUiItem) {
        val termId = currentTerm.value?.id ?: return
        viewModelScope.launch {
            subjectRepository.updateSubject(updated.toDomain(termId))
            _dialogState.value = SubjectDialogState.Hidden
        }
    }

    fun onConfirmDeleteSubject(subjectId: String) {
        viewModelScope.launch {
            subjectRepository.deleteSubject(subjectId)
            _dialogState.value = SubjectDialogState.Hidden
        }
    }

    fun onConfirmCreateSemester(name: String) {
        viewModelScope.launch {
            val newTerm = AcademicTerm(
                id = UUID.randomUUID().toString(),
                name = name.trim(),
                startDate = System.currentTimeMillis(),
                endDate = System.currentTimeMillis(),
                isCurrent = true,
            )
            termRepository.insertTerm(newTerm)
            termRepository.setCurrentTerm(newTerm.id)
            _dialogState.value = SubjectDialogState.Hidden
        }
    }

    fun onSwitchTerm(termId: String) {
        viewModelScope.launch { termRepository.setCurrentTerm(termId) }
    }

    // ── Private helpers ───────────────────────────────────────────────────

    private fun buildUiState(term: AcademicTerm?, subjects: List<Subject>): SubjectUiState {
        if (term == null) return SubjectUiState.NoSemester
        if (subjects.isEmpty()) return SubjectUiState.Empty
        return SubjectUiState.Success(subjects.mapIndexed { i, s -> s.toUiItem(i) })
    }

    private fun Subject.toUiItem(index: Int) = SubjectUiItem(
        id = id,
        name = name,
        type = type,
        minAttendancePercentage = minAttendancePercentage,
        accentColor = SubjectAccentColor.entries[index % SubjectAccentColor.entries.size],
    )

    private fun SubjectUiItem.toDomain(termId: String) = Subject(
        id = id,
        termId = termId,
        name = name,
        type = type,
        minAttendancePercentage = minAttendancePercentage,
    )
}
