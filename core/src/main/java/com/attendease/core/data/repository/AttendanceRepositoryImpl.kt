package com.attendease.core.data.repository

import com.attendease.core.data.database.dao.AttendanceDao
import com.attendease.core.data.mapper.AttendanceRecordMapper.toDomain
import com.attendease.core.data.mapper.AttendanceRecordMapper.toEntity
import com.attendease.core.domain.model.AttendanceRecord
import com.attendease.core.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class AttendanceRepositoryImpl(
    private val attendanceDao: AttendanceDao
) : AttendanceRepository {

    override fun getAllRecords(): Flow<Result<List<AttendanceRecord>>> {
        return attendanceDao.getAllRecords().map { entities ->
            Result.success(entities.map { it.toDomain() })
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    override fun getRecordsForSubject(subjectId: Long): Flow<Result<List<AttendanceRecord>>> {
        return attendanceDao.getRecordsForSubject(subjectId).map { entities ->
            Result.success(entities.map { it.toDomain() })
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    override fun getRecordsForDate(date: LocalDate): Flow<Result<List<AttendanceRecord>>> {
        return attendanceDao.getRecordsForDate(date.toString()).map { entities ->
            Result.success(entities.map { it.toDomain() })
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    override suspend fun insertRecord(record: AttendanceRecord): Result<Long> {
        return try {
            val id = attendanceDao.insertRecord(record.toEntity())
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRecord(record: AttendanceRecord): Result<Unit> {
        return try {
            attendanceDao.updateRecord(record.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteRecord(record: AttendanceRecord): Result<Unit> {
        return try {
            attendanceDao.deleteRecord(record.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
