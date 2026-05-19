package com.classtrack.core.domain.usecase

import com.classtrack.core.domain.model.ScheduleSlot
import com.classtrack.core.domain.model.Subject
import com.classtrack.core.domain.repository.ScheduleSlotRepository
import kotlinx.coroutines.flow.Flow

class GetGeofenceScheduleForDayUseCase(
    private val scheduleSlotRepository: ScheduleSlotRepository
) {
    operator fun invoke(dayOfWeek: Int): Flow<List<Pair<ScheduleSlot, Subject>>> {
        return scheduleSlotRepository.getSlotsWithSubjectsForDay(dayOfWeek)
    }
}
