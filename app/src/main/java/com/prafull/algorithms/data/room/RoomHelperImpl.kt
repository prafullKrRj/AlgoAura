package com.prafull.algorithms.data.room

import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.models.Algorithm

class RoomHelperImpl(
    private val dao: AlgoDao
) : RoomHelper {

    override suspend fun insert(algo: Algorithm) = dao.insert(algo.toEntity())
    override suspend fun delete(algo: Algorithm) = dao.delete(algo.toEntity())
    override fun getAllAlgorithms() = dao.getAllAlgorithms()
    override suspend fun checkIfAlgoExists(id: String): Boolean {
        return dao.checkIfAlgoExists(id).isNotEmpty()
    }

    override suspend fun toggle(algorithm: Algorithm) {
        if (checkIfAlgoExists(algorithm.id)) {
            delete(algorithm)
        } else {
            insert(algorithm)
        }
    }
}