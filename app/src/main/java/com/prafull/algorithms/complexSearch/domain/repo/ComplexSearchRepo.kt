package com.prafull.algorithms.complexSearch.domain.repo

import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.complexSearch.domain.models.ComplexAlgorithm
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageAlgo
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageData
import kotlinx.coroutines.flow.Flow

interface ComplexSearchRepo {

    suspend fun getComplexAlgo(algoName: String): Flow<BaseClass<ComplexAlgorithm>>
    suspend fun getComplexLanguages(): Flow<BaseClass<List<String>>> // Get all the languages
    suspend fun getComplexSearchResults(query: String): Flow<BaseClass<List<String>>>

    suspend fun getComplexLanguageData(lang: String): Flow<BaseClass<ComplexLanguageData>>

    suspend fun getComplexLanguageAlgo(
        lang: String,
        algo: String
    ): Flow<BaseClass<ComplexLanguageAlgo>>
}