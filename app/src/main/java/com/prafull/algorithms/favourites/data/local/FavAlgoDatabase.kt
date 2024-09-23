package com.prafull.algorithms.favourites.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [AlgorithmEntity::class],
    version = 1,
    exportSchema = false
)

abstract class FavAlgoDatabase : RoomDatabase() {
    abstract fun algoDao(): FavAlgoDao
}
