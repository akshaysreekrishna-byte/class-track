package com.classtrack.core.domain.repository

import com.classtrack.core.domain.model.AcademicTerm
import kotlinx.coroutines.flow.Flow

interface AcademicTermRepository {
    fun getAllTerms(): Flow<List<AcademicTerm>>
    fun getCurrentTerm(): Flow<AcademicTerm?>
    suspend fun insertTerm(term: AcademicTerm)
    suspend fun updateTerm(term: AcademicTerm)
    suspend fun deleteTerm(termId: String)
    // Crucial for toggling the active semester without UI-side loop logic
    suspend fun setCurrentTerm(termId: String)
}
