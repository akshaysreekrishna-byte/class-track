package com.classtrack.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.classtrack.core.data.local.entity.ScheduleSlotEntity
import com.classtrack.core.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleSlotDao {
    @Query("SELECT * FROM schedule_slots WHERE subjectId = :subjectId")
    fun getSlotsForSubject(subjectId: String): Flow<List<ScheduleSlotEntity>>

    @Query("""
        SELECT * FROM schedule_slots 
        INNER JOIN subjects ON schedule_slots.subjectId = subjects.id 
        WHERE schedule_slots.dayOfWeek = :dayOfWeek AND subjects.termId = :activeTermId
    """)
    fun getSlotsWithSubjectsForDay(
        dayOfWeek: Int, 
        activeTermId: String
    ): Flow<Map<ScheduleSlotEntity, SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlot(slot: ScheduleSlotEntity)

    @Update
    suspend fun updateSlot(slot: ScheduleSlotEntity)

    @Query("DELETE FROM schedule_slots WHERE id = :slotId")
    suspend fun deleteSlot(slotId: String)
}
