package com.prafull.algorithms.search.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [SearchedEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}