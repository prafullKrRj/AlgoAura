package com.prafull.algorithms

import android.app.Application
import androidx.room.Room
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AlgorithmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AlgorithmApp)
            module {
                single<FirebaseStorage> {
                    FirebaseStorage.getInstance()
                }
                single { provideDatabase(androidApplication()) }
                single { provideAlgoDao(get()) }
                viewModel {
                    AlgoViewModel()
                }
                viewModel {
                    FolderViewModel()
                }
            }
        }
    }
}

fun provideDatabase(application: Application): AlgoDatabase {
    return Room.databaseBuilder(application, AlgoDatabase::class.java, "algo_database")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideAlgoDao(database: AlgoDatabase): AlgoDao {
    return database.algoDao()
}