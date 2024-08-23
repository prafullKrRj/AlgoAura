package com.prafull.algorithms.data

import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.models.Algorithms
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object RoomHelper : KoinComponent {
    private val dao by inject<AlgoDao>()
    suspend fun insert(algo: Algorithms) = dao.insert(algo.toEntity())
    suspend fun delete(algo: Algorithms) = dao.delete(algo.toEntity())
    fun getAllAlgorithms() = dao.getAllAlgorithms()
}