package com.prafull.algorithms.data.firebase

import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.models.ComplexAlgorithm
import com.prafull.algorithms.models.ComplexLanguageAlgo
import com.prafull.algorithms.models.ComplexLanguageData
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.models.FolderInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.flow.Flow

interface FirebaseHelper {
    fun getAlgorithm(fileInfo: FileInfo): Flow<BaseClass<Algorithm>>      // Get the algorithm

    fun getAlgorithms(path: String): Flow<BaseClass<List<FileInfo>>>        // Get all the algorithms in a group
    fun getAlgoGroups(language: ProgrammingLanguage): Flow<BaseClass<List<FolderInfo>>> // Get all the groups of algorithms

    suspend fun getListOfDocuments(query: String): Flow<BaseClass<List<FileInfo>>>


    suspend fun getComplexLanguages(): Flow<BaseClass<List<String>>> // Get all the languages
    suspend fun getComplexSearchResults(query: String): Flow<BaseClass<List<String>>>
    suspend fun getComplexAlgo(algoName: String): Flow<BaseClass<ComplexAlgorithm>>
    suspend fun getComplexLanguageData(lang: String): Flow<BaseClass<ComplexLanguageData>>
    suspend fun getComplexLanguageAlgo(
        lang: String,
        algo: String
    ): Flow<BaseClass<ComplexLanguageAlgo>>
}