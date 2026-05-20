package com.classtrack.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.classtrack.core.data.local.entity.HolidayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holidays WHERE termId = :termId")
    fun getHolidaysForTerm(termId: String): Flow<List<HolidayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoliday(holiday: HolidayEntity)

    @Query("DELETE FROM holidays WHERE id = :holidayId")
    suspend fun deleteHoliday(holidayId: String)
}
