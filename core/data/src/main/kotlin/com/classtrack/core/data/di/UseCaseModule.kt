package com.classtrack.core.data.di

import com.classtrack.core.domain.repository.AcademicTermRepository
import com.classtrack.core.domain.repository.AttendanceRepository
import com.classtrack.core.domain.repository.ScheduleSlotRepository
import com.classtrack.core.domain.repository.SubjectRepository
import com.classtrack.core.domain.usecase.CheckGeofenceUseCase
import com.classtrack.core.domain.usecase.GetGeofenceScheduleForDayUseCase
import com.classtrack.core.domain.usecase.GetSubjectAttendanceStatusUseCase
import com.classtrack.core.domain.usecase.GetSubjectsForCurrentTermUseCase
import com.classtrack.core.domain.usecase.MarkAttendanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides all domain Use Cases as Hilt-managed instances.
 * Centralised here in :core:data so any feature module can inject them
 * without owning domain lifecycle bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetSubjectsForCurrentTermUseCase(
        termRepository: AcademicTermRepository,
        subjectRepository: SubjectRepository,
    ): GetSubjectsForCurrentTermUseCase =
        GetSubjectsForCurrentTermUseCase(termRepository, subjectRepository)

    @Provides
    fun provideGetSubjectAttendanceStatusUseCase(
        subjectRepository: SubjectRepository,
        attendanceRepository: AttendanceRepository,
    ): GetSubjectAttendanceStatusUseCase =
        GetSubjectAttendanceStatusUseCase(subjectRepository, attendanceRepository)

    @Provides
    fun provideMarkAttendanceUseCase(
        attendanceRepository: AttendanceRepository,
    ): MarkAttendanceUseCase =
        MarkAttendanceUseCase(attendanceRepository)

    @Provides
    fun provideCheckGeofenceUseCase(): CheckGeofenceUseCase =
        CheckGeofenceUseCase()

    @Provides
    fun provideGetGeofenceScheduleForDayUseCase(
        scheduleSlotRepository: ScheduleSlotRepository,
    ): GetGeofenceScheduleForDayUseCase =
        GetGeofenceScheduleForDayUseCase(scheduleSlotRepository)
}
