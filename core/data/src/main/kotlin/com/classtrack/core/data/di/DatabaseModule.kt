package com.classtrack.core.data.di

import android.content.Context
import androidx.room.Room
import com.classtrack.core.data.local.ClassTrackDatabase
import com.classtrack.core.data.local.dao.AcademicTermDao
import com.classtrack.core.data.local.dao.AttendanceRecordDao
import com.classtrack.core.data.local.dao.HolidayDao
import com.classtrack.core.data.local.dao.ScheduleSlotDao
import com.classtrack.core.data.local.dao.SubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the Room database and all DAOs as singletons.
 * Lives in :core:data so repository implementations have access to their DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ClassTrackDatabase =
        Room.databaseBuilder(
            context,
            ClassTrackDatabase::class.java,
            "class_track.db",
        ).build()

    @Provides
    @Singleton
    fun provideSubjectDao(db: ClassTrackDatabase): SubjectDao = db.subjectDao

    @Provides
    @Singleton
    fun provideAcademicTermDao(db: ClassTrackDatabase): AcademicTermDao = db.academicTermDao

    @Provides
    @Singleton
    fun provideAttendanceRecordDao(db: ClassTrackDatabase): AttendanceRecordDao =
        db.attendanceRecordDao

    @Provides
    @Singleton
    fun provideScheduleSlotDao(db: ClassTrackDatabase): ScheduleSlotDao = db.scheduleSlotDao

    @Provides
    @Singleton
    fun provideHolidayDao(db: ClassTrackDatabase): HolidayDao = db.holidayDao
}
