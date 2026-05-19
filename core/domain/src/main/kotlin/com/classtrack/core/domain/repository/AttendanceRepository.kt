package com.classtrack.core.domain.repository

import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.model.AttendanceStatus
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun getRecordsForSubject(subjectId: String): Flow<List<AttendanceRecord>>
    // Perfect for weekly/monthly analytics graphs
    fun getRecordsForSubjectInTimeRange(subjectId: String, startTime: Long, endTime: Long): Flow<List<AttendanceRecord>>
    fun getRecordsForSlot(slotId: String): Flow<List<AttendanceRecord>>
    // Direct counts let Room/SQL do the math instantly on the disk layer!
    fun getAttendanceCountForSubject(subjectId: String, status: AttendanceStatus): Flow<Int>
    suspend fun insertRecord(record: AttendanceRecord)
    suspend fun updateRecord(record: AttendanceRecord)
    suspend fun deleteRecord(recordId: String)
}
