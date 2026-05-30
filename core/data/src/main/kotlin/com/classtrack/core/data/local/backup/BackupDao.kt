package com.classtrack.core.data.local.backup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.classtrack.core.data.local.entity.AcademicTermEntity
import com.classtrack.core.data.local.entity.AttendanceRecordEntity
import com.classtrack.core.data.local.entity.HolidayEntity
import com.classtrack.core.data.local.entity.ScheduleSlotEntity
import com.classtrack.core.data.local.entity.SubjectEntity

@Dao
interface BackupDao {

    @Query("SELECT * FROM academic_terms")
    suspend fun getAllTerms(): List<AcademicTermEntity>

    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjects(): List<SubjectEntity>

    @Query("SELECT * FROM schedule_slots")
    suspend fun getAllScheduleSlots(): List<ScheduleSlotEntity>

    @Query("SELECT * FROM holidays")
    suspend fun getAllHolidays(): List<HolidayEntity>

    @Query("SELECT * FROM attendance_records")
    suspend fun getAllAttendanceRecords(): List<AttendanceRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerms(terms: List<AcademicTermEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleSlots(slots: List<ScheduleSlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolidays(holidays: List<HolidayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecords(records: List<AttendanceRecordEntity>)

    @Query("DELETE FROM attendance_records")
    suspend fun clearAttendanceRecords()

    @Query("DELETE FROM schedule_slots")
    suspend fun clearScheduleSlots()

    @Query("DELETE FROM subjects")
    suspend fun clearSubjects()

    @Query("DELETE FROM holidays")
    suspend fun clearHolidays()

    @Query("DELETE FROM academic_terms")
    suspend fun clearTerms()
}
