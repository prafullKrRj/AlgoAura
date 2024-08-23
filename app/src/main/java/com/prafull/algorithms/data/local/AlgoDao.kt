package com.prafull.algorithms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlgoDao {
    @Query("SELECT * FROM AlgorithmEntity")
    fun getAllAlgorithms(): Flow<List<AlgorithmEntity>>

    @Insert
    suspend fun insert(algo: AlgorithmEntity)

    @Delete
    suspend fun delete(algo: AlgorithmEntity)
}
