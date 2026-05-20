package com.classtrack.core.data.repository

import com.classtrack.core.data.local.dao.AttendanceRecordDao
import com.classtrack.core.data.mapper.toDomain
import com.classtrack.core.data.mapper.toEntity
import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.model.AttendanceStatus
import com.classtrack.core.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttendanceRepositoryImpl(
    private val attendanceRecordDao: AttendanceRecordDao
) : AttendanceRepository {
    override fun getRecordsForSubject(subjectId: String): Flow<List<AttendanceRecord>> {
        return attendanceRecordDao.getRecordsForSubject(subjectId).map { list -> list.map { it.toDomain() } }
    }

    override fun getRecordsForSubjectInTimeRange(
        subjectId: String,
        startTimeUtc: Long,
        endTimeUtc: Long
    ): Flow<List<AttendanceRecord>> {
        return attendanceRecordDao.getRecordsForSubjectInTimeRange(subjectId, startTimeUtc, endTimeUtc)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getRecordsForSlot(slotId: String): Flow<List<AttendanceRecord>> {
        return attendanceRecordDao.getRecordsForSlot(slotId).map { list -> list.map { it.toDomain() } }
    }

    override fun getAttendanceCountForSubject(subjectId: String, status: AttendanceStatus): Flow<Int> {
        return attendanceRecordDao.getAttendanceCountForSubject(subjectId, status.name)
    }

    override suspend fun insertRecord(record: AttendanceRecord) {
        attendanceRecordDao.insertRecord(record.toEntity())
    }

    override suspend fun updateRecord(record: AttendanceRecord) {
        attendanceRecordDao.updateRecord(record.toEntity())
    }

    override suspend fun deleteRecord(recordId: String) {
        attendanceRecordDao.deleteRecord(recordId)
    }
}
