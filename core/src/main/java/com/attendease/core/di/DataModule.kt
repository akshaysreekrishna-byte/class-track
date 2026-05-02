package com.attendease.core.di

import android.content.Context
import androidx.room.Room
import com.attendease.core.data.database.AppDatabase
import com.attendease.core.data.database.dao.AttendanceDao
import com.attendease.core.data.database.dao.SubjectDao
import com.attendease.core.data.repository.AttendanceRepositoryImpl
import com.attendease.core.data.repository.SubjectRepositoryImpl
import com.attendease.core.domain.repository.AttendanceRepository
import com.attendease.core.domain.repository.SubjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "attendease_db"
        ).build()
    }

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    fun provideAttendanceDao(database: AppDatabase): AttendanceDao {
        return database.attendanceDao()
    }

    @Provides
    @Singleton
    fun provideSubjectRepository(subjectDao: SubjectDao): SubjectRepository {
        return SubjectRepositoryImpl(subjectDao)
    }

    @Provides
    @Singleton
    fun provideAttendanceRepository(attendanceDao: AttendanceDao): AttendanceRepository {
        return AttendanceRepositoryImpl(attendanceDao)
    }
}
