package com.classtrack.core.domain.repository

import com.classtrack.core.domain.model.Holiday
import kotlinx.coroutines.flow.Flow

interface HolidayRepository {
    fun getHolidaysForTerm(termId: String): Flow<List<Holiday>>
    suspend fun insertHoliday(holiday: Holiday)
    suspend fun deleteHoliday(holidayId: String)
}
