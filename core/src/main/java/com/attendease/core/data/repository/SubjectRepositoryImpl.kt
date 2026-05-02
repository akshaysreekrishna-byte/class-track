package com.attendease.core.data.repository

import com.attendease.core.data.database.dao.SubjectDao
import com.attendease.core.data.mapper.SubjectMapper.toDomain
import com.attendease.core.data.mapper.SubjectMapper.toEntity
import com.attendease.core.domain.model.Subject
import com.attendease.core.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SubjectRepositoryImpl(
    private val subjectDao: SubjectDao
) : SubjectRepository {

    override fun getAllSubjects(): Flow<Result<List<Subject>>> {
        return subjectDao.getAllSubjects().map { entities ->
            Result.success(entities.map { it.toDomain() })
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    override fun getSubjectById(id: Long): Flow<Result<Subject?>> {
        return subjectDao.getSubjectById(id).map { entity ->
            Result.success(entity?.toDomain())
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    override suspend fun insertSubject(subject: Subject): Result<Long> {
        return try {
            val id = subjectDao.insertSubject(subject.toEntity())
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSubject(subject: Subject): Result<Unit> {
        return try {
            subjectDao.updateSubject(subject.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSubject(subject: Subject): Result<Unit> {
        return try {
            subjectDao.deleteSubject(subject.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
