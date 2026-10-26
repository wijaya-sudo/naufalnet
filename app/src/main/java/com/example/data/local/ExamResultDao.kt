package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ExamResultEntity): Long

    @Query("SELECT * FROM exam_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<ExamResultEntity>>

    @Query("SELECT * FROM exam_results WHERE id = :resultId")
    suspend fun getResultById(resultId: Long): ExamResultEntity?

    @Query("DELETE FROM exam_results WHERE id = :resultId")
    suspend fun deleteResult(resultId: Long)

    @Query("DELETE FROM exam_results")
    suspend fun clearAll()
}
