package com.prafull.algorithms.data

import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.models.Algorithm
import javax.inject.Inject

class RoomHelperImpl @Inject constructor(
    private val dao: AlgoDao
) : RoomHelper {

    override suspend fun insert(algo: Algorithm) = dao.insert(algo.toEntity())
    override suspend fun delete(algo: Algorithm) = dao.delete(algo.toEntity())
    override fun getAllAlgorithms() = dao.getAllAlgorithms()
}