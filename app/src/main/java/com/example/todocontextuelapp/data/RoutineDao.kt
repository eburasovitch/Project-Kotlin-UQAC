package com.example.todocontextuelapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine_table")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM routine_table WHERE id = :id LIMIT 1")
    suspend fun getRoutineById(id: Int): Routine?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoutine(routine: Routine): Long

    @Update
    suspend fun updateRoutine(routine: Routine): Int

    @Delete
    suspend fun deleteRoutine(routine: Routine): Int
}
