package com.classtrack.core.domain.repository

import com.classtrack.core.domain.model.ScheduleSlot
import com.classtrack.core.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface ScheduleSlotRepository {
    fun getSlotsForSubject(subjectId: String): Flow<List<ScheduleSlot>>
    // Fetches only the slots for the active semester/term
    fun getSlotsForDayInCurrentTerm(dayOfWeek: Int): Flow<List<ScheduleSlot>>
    // Super useful for a "Today's Schedule" UI view or background worker
    fun getSlotsWithSubjectsForDay(dayOfWeek: Int): Flow<List<Pair<ScheduleSlot, Subject>>>
    suspend fun insertSlot(slot: ScheduleSlot)
    suspend fun updateSlot(slot: ScheduleSlot)
    suspend fun deleteSlot(slotId: String)
}
