package com.classtrack.core.data.repository

import com.classtrack.core.data.local.dao.AcademicTermDao
import com.classtrack.core.data.local.dao.ScheduleSlotDao
import com.classtrack.core.data.mapper.toDomain
import com.classtrack.core.data.mapper.toEntity
import com.classtrack.core.domain.model.ScheduleSlot
import com.classtrack.core.domain.model.Subject
import com.classtrack.core.domain.repository.ScheduleSlotRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ScheduleSlotRepositoryImpl(
    private val scheduleSlotDao: ScheduleSlotDao,
    private val academicTermDao: AcademicTermDao
) : ScheduleSlotRepository {
    override fun getSlotsForSubject(subjectId: String): Flow<List<ScheduleSlot>> {
        return scheduleSlotDao.getSlotsForSubject(subjectId).map { list -> list.map { it.toDomain() } }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSlotsForDayInCurrentTerm(dayOfWeek: Int): Flow<List<ScheduleSlot>> {
        return academicTermDao.getCurrentTerm()
            .filterNotNull()
            .flatMapLatest { activeTerm ->
                scheduleSlotDao.getSlotsForDayInTerm(dayOfWeek, activeTerm.id)
            }.map { list -> list.map { it.toDomain() } }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSlotsWithSubjectsForDay(dayOfWeek: Int): Flow<List<Pair<ScheduleSlot, Subject>>> {
        return academicTermDao.getCurrentTerm()
            .filterNotNull()
            .flatMapLatest { activeTerm ->
                scheduleSlotDao.getSlotsWithSubjectsForDay(dayOfWeek, activeTerm.id)
            }.map { entityMap ->
                entityMap.map { (slotEntity, subjectEntity) ->
                    slotEntity.toDomain() to subjectEntity.toDomain()
                }
            }
    }

    override suspend fun insertSlot(slot: ScheduleSlot) {
        scheduleSlotDao.insertSlot(slot.toEntity())
    }

    override suspend fun updateSlot(slot: ScheduleSlot) {
        scheduleSlotDao.updateSlot(slot.toEntity())
    }

    override suspend fun deleteSlot(slotId: String) {
        scheduleSlotDao.deleteSlot(slotId)
    }
}
