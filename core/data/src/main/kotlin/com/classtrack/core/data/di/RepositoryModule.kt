package com.classtrack.core.data.di

import com.classtrack.core.data.local.dao.AcademicTermDao
import com.classtrack.core.data.local.dao.AttendanceRecordDao
import com.classtrack.core.data.local.dao.HolidayDao
import com.classtrack.core.data.local.dao.ScheduleSlotDao
import com.classtrack.core.data.local.dao.SubjectDao
import com.classtrack.core.data.repository.AcademicTermRepositoryImpl
import com.classtrack.core.data.repository.AttendanceRepositoryImpl
import com.classtrack.core.data.repository.HolidayRepositoryImpl
import com.classtrack.core.data.repository.ScheduleSlotRepositoryImpl
import com.classtrack.core.data.repository.SubjectRepositoryImpl
import com.classtrack.core.domain.repository.AcademicTermRepository
import com.classtrack.core.domain.repository.AttendanceRepository
import com.classtrack.core.domain.repository.HolidayRepository
import com.classtrack.core.domain.repository.ScheduleSlotRepository
import com.classtrack.core.domain.repository.SubjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds all repository interfaces to their data-layer implementations.
 * Implementations receive their DAOs from [DatabaseModule].
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSubjectRepository(dao: SubjectDao): SubjectRepository =
        SubjectRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAcademicTermRepository(dao: AcademicTermDao): AcademicTermRepository =
        AcademicTermRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAttendanceRepository(dao: AttendanceRecordDao): AttendanceRepository =
        AttendanceRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideScheduleSlotRepository(dao: ScheduleSlotDao, academicTermDao: AcademicTermDao): ScheduleSlotRepository =
        ScheduleSlotRepositoryImpl(dao, academicTermDao)

    @Provides
    @Singleton
    fun provideHolidayRepository(dao: HolidayDao): HolidayRepository =
        HolidayRepositoryImpl(dao)
}
