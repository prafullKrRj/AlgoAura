package com.prafull.algorithms.codeScreen.domain

import com.prafull.algorithms.commons.models.Algorithm
import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.utils.BaseClass
import kotlinx.coroutines.flow.Flow

interface CodeRepository {

    suspend fun checkIfAlgoExists(id: String): Boolean
    suspend fun toggle(algorithm: Algorithm)
    suspend fun insert(algo: Algorithm)

    // get algorithms into a particular folder in home screen like C++ -> Bit Manipulation -> algos -> particular algorithm
    fun getAlgorithm(fileInfo: FileInfo): Flow<BaseClass<Algorithm>>      // Get the algorithm
}