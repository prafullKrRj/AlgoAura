package com.prafull.algorithms.data.room

import com.prafull.algorithms.data.local.AlgorithmEntity
import com.prafull.algorithms.models.Algorithm
import kotlinx.coroutines.flow.Flow

interface RoomHelper {
    suspend fun insert(algo: Algorithm)
    suspend fun delete(algo: Algorithm)
    fun getAllAlgorithms(): Flow<List<AlgorithmEntity>>
}