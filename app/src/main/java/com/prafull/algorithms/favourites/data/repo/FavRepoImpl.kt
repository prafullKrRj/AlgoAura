package com.prafull.algorithms.favourites.data.repo

import com.prafull.algorithms.commons.models.Algorithm
import com.prafull.algorithms.favourites.data.local.AlgorithmEntity
import com.prafull.algorithms.favourites.data.local.FavAlgoDao
import com.prafull.algorithms.favourites.domain.FavRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavRepoImpl : FavRepo, KoinComponent {
    private val dao: FavAlgoDao by inject()

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


    override suspend fun deleteAlgos(selectedAlgos: List<AlgorithmEntity>) {
        dao.deleteAlgos(selectedAlgos)
    }
}