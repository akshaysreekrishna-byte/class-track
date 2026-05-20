package com.classtrack.core.data.repository

import com.classtrack.core.data.local.dao.HolidayDao
import com.classtrack.core.data.mapper.toDomain
import com.classtrack.core.data.mapper.toEntity
import com.classtrack.core.domain.model.Holiday
import com.classtrack.core.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HolidayRepositoryImpl(
    private val holidayDao: HolidayDao
) : HolidayRepository {
    override fun getHolidaysForTerm(termId: String): Flow<List<Holiday>> {
        return holidayDao.getHolidaysForTerm(termId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertHoliday(holiday: Holiday) {
        holidayDao.insertHoliday(holiday.toEntity())
    }

    override suspend fun deleteHoliday(holidayId: String) {
        holidayDao.deleteHoliday(holidayId)
    }
}
