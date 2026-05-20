package com.classtrack.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.classtrack.core.data.local.entity.AttendanceRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceRecordDao {
    @Query("SELECT * FROM attendance_records WHERE subjectId = :subjectId")
    fun getRecordsForSubject(subjectId: String): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT * FROM attendance_records WHERE subjectId = :subjectId AND timestamp BETWEEN :startTime AND :endTime")
    fun getRecordsForSubjectInTimeRange(
        subjectId: String,
        startTime: Long,
        endTime: Long
    ): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT * FROM attendance_records WHERE scheduleSlotId = :slotId")
    fun getRecordsForSlot(slotId: String): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT COUNT(*) FROM attendance_records WHERE subjectId = :subjectId AND status = :statusText")
    fun getAttendanceCountForSubject(subjectId: String, statusText: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AttendanceRecordEntity)

    @Update
    suspend fun updateRecord(record: AttendanceRecordEntity)

    @Query("DELETE FROM attendance_records WHERE id = :recordId")
    suspend fun deleteRecord(recordId: String)
}
