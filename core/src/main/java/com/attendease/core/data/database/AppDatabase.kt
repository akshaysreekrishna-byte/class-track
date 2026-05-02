package com.attendease.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.attendease.core.data.database.dao.AttendanceDao
import com.attendease.core.data.database.dao.SubjectDao
import com.attendease.core.data.database.entity.AttendanceRecordEntity
import com.attendease.core.data.database.entity.SubjectEntity

@Database(
    entities = [SubjectEntity::class, AttendanceRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun attendanceDao(): AttendanceDao
}
