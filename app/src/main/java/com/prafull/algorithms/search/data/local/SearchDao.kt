package com.prafull.algorithms.search.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Dao
interface SearchDao {

    @Query("SELECT * FROM SearchedEntity order by lastTime desc")
    fun getAllSearched(): Flow<List<SearchedEntity>>

    @Upsert
    suspend fun insertSearchedText(searchedEntity: SearchedEntity)

    @Delete
    suspend fun deleteSearched(entities: List<SearchedEntity>)

    suspend fun deleteAllSearched() {
        getAllSearched().collectLatest {
            deleteSearched(it)
        }
    }


}