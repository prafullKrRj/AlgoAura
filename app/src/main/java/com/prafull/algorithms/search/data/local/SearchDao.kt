package com.prafull.algorithms.search.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT * FROM SearchedEntity order by lastTime desc")
    fun getAllSearched(): Flow<List<SearchedEntity>>

    @Upsert
    suspend fun insertSearchedText(searchedEntity: SearchedEntity)

}