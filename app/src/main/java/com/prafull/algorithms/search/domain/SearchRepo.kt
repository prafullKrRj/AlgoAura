package com.prafull.algorithms.search.domain

import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.search.data.local.SearchedEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepo {
    suspend fun insertSearchedText(searchedEntity: SearchedEntity)
    fun getSearchedAlgorithms(): Flow<List<SearchedEntity>>
    suspend fun getListOfDocuments(query: String): Flow<BaseClass<List<FileInfo>>>
}