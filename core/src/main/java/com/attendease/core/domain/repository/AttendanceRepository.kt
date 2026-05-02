package com.attendease.core.domain.repository

import com.attendease.core.domain.model.AttendanceRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface AttendanceRepository {
    fun getAllRecords(): Flow<Result<List<AttendanceRecord>>>
    fun getRecordsForSubject(subjectId: Long): Flow<Result<List<AttendanceRecord>>>
    fun getRecordsForDate(date: LocalDate): Flow<Result<List<AttendanceRecord>>>
    suspend fun insertRecord(record: AttendanceRecord): Result<Long>
    suspend fun updateRecord(record: AttendanceRecord): Result<Unit>
    suspend fun deleteRecord(record: AttendanceRecord): Result<Unit>
}
