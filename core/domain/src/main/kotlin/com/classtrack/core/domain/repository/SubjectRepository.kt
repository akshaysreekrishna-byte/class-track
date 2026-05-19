package com.classtrack.core.domain.repository

import com.classtrack.core.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    fun getSubjectsForTerm(termId: String): Flow<List<Subject>>
    fun getSubjectById(subjectId: String): Flow<Subject?>
    suspend fun insertSubject(subject: Subject)
    suspend fun updateSubject(subject: Subject)
    suspend fun deleteSubject(subjectId: String)
}
