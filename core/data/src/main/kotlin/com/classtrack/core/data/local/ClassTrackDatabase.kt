package com.classtrack.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.classtrack.core.data.local.dao.AcademicTermDao
import com.classtrack.core.data.local.dao.AttendanceRecordDao
import com.classtrack.core.data.local.dao.ScheduleSlotDao
import com.classtrack.core.data.local.dao.SubjectDao
import com.classtrack.core.data.local.entity.AcademicTermEntity
import com.classtrack.core.data.local.entity.AttendanceRecordEntity
import com.classtrack.core.data.local.entity.ScheduleSlotEntity
import com.classtrack.core.data.local.entity.SubjectEntity

@Database(
    entities = [
        AcademicTermEntity::class,
        SubjectEntity::class,
        ScheduleSlotEntity::class,
        AttendanceRecordEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ClassTrackDatabase : RoomDatabase() {
    abstract val academicTermDao: AcademicTermDao
    abstract val subjectDao: SubjectDao
    abstract val scheduleSlotDao: ScheduleSlotDao
    abstract val attendanceRecordDao: AttendanceRecordDao
}
