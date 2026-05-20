package com.classtrack.core.data.repository

import com.classtrack.core.data.local.dao.AcademicTermDao
import com.classtrack.core.data.mapper.toDomain
import com.classtrack.core.data.mapper.toEntity
import com.classtrack.core.domain.model.AcademicTerm
import com.classtrack.core.domain.repository.AcademicTermRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AcademicTermRepositoryImpl(
    private val academicTermDao: AcademicTermDao
) : AcademicTermRepository {
    override fun getAllTerms(): Flow<List<AcademicTerm>> {
        return academicTermDao.getAllTerms().map { list -> list.map { it.toDomain() } }
    }

    override fun getCurrentTerm(): Flow<AcademicTerm?> {
        return academicTermDao.getCurrentTerm().map { it?.toDomain() }
    }

    override suspend fun insertTerm(term: AcademicTerm) {
        academicTermDao.insertTerm(term.toEntity())
    }

    override suspend fun updateTerm(term: AcademicTerm) {
        academicTermDao.updateTerm(term.toEntity())
    }

    override suspend fun deleteTerm(termId: String) {
        academicTermDao.deleteTerm(termId)
    }

    override suspend fun setCurrentTerm(termId: String) {
        academicTermDao.setCurrentTerm(termId)
    }
}
