package com.classtrack.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.classtrack.core.data.local.entity.AcademicTermEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademicTermDao {
    @Query("SELECT * FROM academic_terms")
    fun getAllTerms(): Flow<List<AcademicTermEntity>>

    @Query("SELECT * FROM academic_terms WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentTerm(): Flow<AcademicTermEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerm(term: AcademicTermEntity)

    @Update
    suspend fun updateTerm(term: AcademicTermEntity)

    @Query("DELETE FROM academic_terms WHERE id = :termId")
    suspend fun deleteTerm(termId: String)

    @Transaction
    suspend fun setCurrentTerm(termId: String) {
        clearCurrentTerms()
        setTermCurrent(termId)
    }

    @Query("UPDATE academic_terms SET isCurrent = 0")
    suspend fun clearCurrentTerms()

    @Query("UPDATE academic_terms SET isCurrent = 1 WHERE id = :termId")
    suspend fun setTermCurrent(termId: String)
}
