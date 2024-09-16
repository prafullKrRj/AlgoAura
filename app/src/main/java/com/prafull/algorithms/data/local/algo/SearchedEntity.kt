package com.prafull.algorithms.data.local.algo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity
data class SearchedEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val searchedText: String,
    val lastTime: Long = System.currentTimeMillis()
)