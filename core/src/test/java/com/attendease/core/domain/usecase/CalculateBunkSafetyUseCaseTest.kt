package com.attendease.core.domain.usecase

import com.attendease.core.domain.model.AttendanceRecord
import com.attendease.core.domain.model.AttendanceStatus
import com.attendease.core.domain.model.Subject
import com.attendease.core.domain.model.SubjectType
import com.attendease.core.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class CalculateBunkSafetyUseCaseTest {

    private val mockSubject = Subject(
        id = 1L,
        name = "Math",
        type = SubjectType.THEORY,
        requiredPercentage = 75.0,
        geofenceLat = null,
        geofenceLng = null,
        geofenceRadius = null
    )

    private val fakeRepository = object : AttendanceRepository {
        override fun getAllRecords(): Flow<Result<List<AttendanceRecord>>> = flowOf(Result.success(emptyList()))
        
        override fun getRecordsForSubject(subjectId: Long): Flow<Result<List<AttendanceRecord>>> {
            // Return 3 present, 1 absent -> Total 4. 3/4 = 75%
            val records = listOf(
                AttendanceRecord(1, subjectId, LocalDate.now(), AttendanceStatus.PRESENT),
                AttendanceRecord(2, subjectId, LocalDate.now(), AttendanceStatus.PRESENT),
                AttendanceRecord(3, subjectId, LocalDate.now(), AttendanceStatus.PRESENT),
                AttendanceRecord(4, subjectId, LocalDate.now(), AttendanceStatus.ABSENT)
            )
            return flowOf(Result.success(records))
        }

        override fun getRecordsForDate(date: LocalDate) = flowOf(Result.success(emptyList<AttendanceRecord>()))
        override suspend fun insertRecord(record: AttendanceRecord) = Result.success(1L)
        override suspend fun updateRecord(record: AttendanceRecord) = Result.success(Unit)
        override suspend fun deleteRecord(record: AttendanceRecord) = Result.success(Unit)
    }

    @Test
    fun `invoke returns correct BunkResult using repository data`() = runBlocking {
        val useCase = CalculateBunkSafetyUseCase(fakeRepository)
        val result = useCase(mockSubject).first()

        assertEquals(75.0, result.currentPercentage, 0.01)
        assertEquals(0, result.safeBunksRemaining)
        assertEquals(0, result.classesNeededToRecover)
    }
}
