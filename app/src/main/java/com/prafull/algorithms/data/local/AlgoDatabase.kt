package com.prafull.algorithms.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [AlgorithmEntity::class], version = 1, exportSchema = false)

abstract class AlgoDatabase : RoomDatabase() {
    abstract fun algoDao(): AlgoDao
}
