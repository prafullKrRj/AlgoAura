package com.prafull.algorithms

import android.app.Application
import androidx.room.Room
import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.data.local.AlgoDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlgorithmApp : Application()


fun provideDatabase(application: Application): AlgoDatabase {
    return Room.databaseBuilder(application, AlgoDatabase::class.java, "algo_database")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideAlgoDao(database: AlgoDatabase): AlgoDao {
    return database.algoDao()
}