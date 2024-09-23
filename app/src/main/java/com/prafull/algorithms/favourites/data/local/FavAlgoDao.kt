package com.prafull.algorithms.favourites.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavAlgoDao {
    @Query("SELECT * FROM AlgorithmEntity order by time desc")
    fun getAllAlgorithms(): Flow<List<AlgorithmEntity>>

    @Insert
    suspend fun insert(algo: AlgorithmEntity)

    @Delete
    suspend fun delete(algo: AlgorithmEntity)

    @Query("SELECT * FROM AlgorithmEntity WHERE id = :id")
    suspend fun checkIfAlgoExists(id: String): List<AlgorithmEntity>


    @Delete
    suspend fun deleteAlgos(selectedAlgos: List<AlgorithmEntity>)
}
