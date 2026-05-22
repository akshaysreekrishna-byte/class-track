package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.model.Subject
import com.classtrack.core.domain.repository.AcademicTermRepository
import com.classtrack.core.domain.repository.SubjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Combines [AcademicTermRepository.getCurrentTerm] with [SubjectRepository.getSubjectsForTerm]
 * so callers never need to manage the term ID directly.
 *
 * Emits an empty list when no current term is set — the UI can distinguish this
 * from a genuinely empty subject list by checking the term state separately.
 */
class GetSubjectsForCurrentTermUseCase(
    private val termRepository: AcademicTermRepository,
    private val subjectRepository: SubjectRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Subject>> =
        termRepository.getCurrentTerm().flatMapLatest { term ->
            if (term == null) flowOf(emptyList())
            else subjectRepository.getSubjectsForTerm(term.id)
        }
}
