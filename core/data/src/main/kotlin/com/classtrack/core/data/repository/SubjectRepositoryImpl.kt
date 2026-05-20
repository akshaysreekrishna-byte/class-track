package com.classtrack.core.data.repository

import com.classtrack.core.data.local.dao.SubjectDao
import com.classtrack.core.data.mapper.toDomain
import com.classtrack.core.data.mapper.toEntity
import com.classtrack.core.domain.model.Subject
import com.classtrack.core.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectRepositoryImpl(
    private val subjectDao: SubjectDao
) : SubjectRepository {
    override fun getSubjectsForTerm(termId: String): Flow<List<Subject>> {
        return subjectDao.getSubjectsForTerm(termId).map { list -> list.map { it.toDomain() } }
    }

    override fun getSubjectById(subjectId: String): Flow<Subject?> {
        return subjectDao.getSubjectById(subjectId).map { it?.toDomain() }
    }

    override suspend fun insertSubject(subject: Subject) {
        subjectDao.insertSubject(subject.toEntity())
    }

    override suspend fun updateSubject(subject: Subject) {
        subjectDao.updateSubject(subject.toEntity())
    }

    override suspend fun deleteSubject(subjectId: String) {
        subjectDao.deleteSubject(subjectId)
    }
}
