package com.prafull.algorithms.search.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SearchedEntity(
    @PrimaryKey
    val searchedText: String,
    val lastTime: Long = System.currentTimeMillis()
)