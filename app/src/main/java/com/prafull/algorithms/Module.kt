package com.prafull.algorithms

import android.content.Context
import androidx.room.Room
import com.prafull.algorithms.data.RoomHelper
import com.prafull.algorithms.data.RoomHelperImpl
import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.data.local.AlgoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AlgoDatabase {
        return Room.databaseBuilder(
            context,
            AlgoDatabase::class.java,
            "algo_db"
        ).build()
    }

    @Provides
    fun provideAlgoDao(appDatabase: AlgoDatabase): AlgoDao {
        return appDatabase.algoDao()
    }

    @Provides
    fun providesRoomHelper(dao: AlgoDao): RoomHelper {
        return RoomHelperImpl(dao)
    }

}