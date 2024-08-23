package com.prafull.algorithms.data

import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.models.Algorithms
import javax.inject.Inject

class RoomHelper @Inject constructor(
    private val dao: AlgoDao
) {

    suspend fun insert(algo: Algorithms) = dao.insert(algo.toEntity())
    suspend fun delete(algo: Algorithms) = dao.delete(algo.toEntity())
    fun getAllAlgorithms() = dao.getAllAlgorithms()
}