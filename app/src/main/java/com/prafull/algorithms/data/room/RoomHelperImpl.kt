package com.prafull.algorithms.data.room

import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.data.local.AlgorithmEntity
import com.prafull.algorithms.data.local.SearchedEntity
import com.prafull.algorithms.models.Algorithm
import kotlinx.coroutines.flow.Flow

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

    override fun getSearchedAlgorithms(): Flow<List<SearchedEntity>> = dao.getAllSearched()
    override suspend fun insertSearchedText(searchedEntity: SearchedEntity) =
        dao.insertSearchedText(searchedEntity)

    override suspend fun deleteAlgos(selectedAlgos: List<AlgorithmEntity>) {
        dao.deleteAlgos(selectedAlgos)
    }
}