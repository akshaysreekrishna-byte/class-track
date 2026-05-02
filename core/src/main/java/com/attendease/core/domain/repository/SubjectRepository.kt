package com.attendease.core.domain.repository

import com.attendease.core.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    fun getAllSubjects(): Flow<Result<List<Subject>>>
    fun getSubjectById(id: Long): Flow<Result<Subject?>>
    suspend fun insertSubject(subject: Subject): Result<Long>
    suspend fun updateSubject(subject: Subject): Result<Unit>
    suspend fun deleteSubject(subject: Subject): Result<Unit>
}
