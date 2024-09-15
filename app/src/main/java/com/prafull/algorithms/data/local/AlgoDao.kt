package com.prafull.algorithms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlgoDao {
    @Query("SELECT * FROM AlgorithmEntity order by time desc")
    fun getAllAlgorithms(): Flow<List<AlgorithmEntity>>

    @Insert
    suspend fun insert(algo: AlgorithmEntity)

    @Delete
    suspend fun delete(algo: AlgorithmEntity)

    @Query("SELECT * FROM AlgorithmEntity WHERE id = :id")
    suspend fun checkIfAlgoExists(id: String): List<AlgorithmEntity>

    @Query("SELECT * FROM SearchedEntity order by lastTime desc")
    fun getAllSearched(): Flow<List<SearchedEntity>>

    @Upsert
    suspend fun insertSearchedText(searchedEntity: SearchedEntity)

    @Delete
    suspend fun deleteAlgos(selectedAlgos: List<AlgorithmEntity>)
}
