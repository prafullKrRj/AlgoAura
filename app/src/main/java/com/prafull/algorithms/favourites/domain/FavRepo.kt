package com.prafull.algorithms.favourites.domain

import com.prafull.algorithms.commons.models.Algorithm
import com.prafull.algorithms.favourites.data.local.AlgorithmEntity
import kotlinx.coroutines.flow.Flow

interface FavRepo {

    suspend fun insert(algo: Algorithm)
    suspend fun delete(algo: Algorithm)

    suspend fun checkIfAlgoExists(id: String): Boolean
    suspend fun toggle(algorithm: Algorithm)
    suspend fun deleteAlgos(selectedAlgos: List<AlgorithmEntity>)
    fun getAllAlgorithms(): Flow<List<AlgorithmEntity>>
}