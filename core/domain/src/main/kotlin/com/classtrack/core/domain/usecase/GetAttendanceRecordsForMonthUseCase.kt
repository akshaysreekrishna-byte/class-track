package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.model.AttendanceRecord
import com.classtrack.core.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Returns all [AttendanceRecord]s for a subject within a calendar month,
 * pre-grouped by [LocalDate] for O(1) lookup in the calendar grid.
 *
 * This implements the optimization from the Phase 5 review: the groupBy
 * is applied once in the use case layer so the calendar grid never
 * needs to scan the full list per cell — each day cell does a single map lookup.
 *
 * @param subjectId  The subject to query.
 * @param year       Calendar year (e.g. 2024).
 * @param month      Calendar month 1–12 (e.g. 9 for September).
 * @return           Flow emitting Map of date → records, rebuilt on every DB write.
 */
class GetAttendanceRecordsForMonthUseCase(
    private val attendanceRepository: AttendanceRepository,
) {
    operator fun invoke(
        subjectId: String,
        year: Int,
        month: Int,
    ): Flow<Map<LocalDate, List<AttendanceRecord>>> {
        val (startMs, endMs) = monthBoundsMs(year, month)
        return attendanceRepository
            .getRecordsForSubjectInTimeRange(subjectId, startMs, endMs)
            .map { records ->
                records.groupBy { record -> epochMsToLocalDate(record.timestamp) }
            }
    }

    /**
     * Returns (startMs inclusive, endMs exclusive) for the given month.
     * Uses epoch day arithmetic to avoid Calendar/Date dependencies.
     */
    private fun monthBoundsMs(year: Int, month: Int): Pair<Long, Long> {
        val startDate = LocalDate(year, month, 1)
        val endDate = nextMonthFirstDay(year, month)
        val tz = TimeZone.currentSystemDefault()
        return startDate.toEpochMs(tz) to endDate.toEpochMs(tz)
    }

    private fun nextMonthFirstDay(year: Int, month: Int): LocalDate =
        if (month == 12) LocalDate(year + 1, 1, 1)
        else LocalDate(year, month + 1, 1)

    private fun LocalDate.toEpochMs(tz: TimeZone): Long {
        // Build epoch-days from LocalDate then convert to milliseconds
        val epochDays = this.toEpochDays()
        // Each day is 86400 seconds; multiply to ms
        return epochDays * 86_400_000L
    }

    private fun epochMsToLocalDate(epochMs: Long): LocalDate {
        val tz = TimeZone.currentSystemDefault()
        return Instant.fromEpochMilliseconds(epochMs)
            .toLocalDateTime(tz)
            .date
    }
}
