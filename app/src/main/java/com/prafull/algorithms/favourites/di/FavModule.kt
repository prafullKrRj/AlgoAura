package com.prafull.algorithms.favourites.di

import androidx.room.Room
import com.prafull.algorithms.favourites.FavouritesViewModel
import com.prafull.algorithms.favourites.data.local.FavAlgoDao
import com.prafull.algorithms.favourites.data.local.FavAlgoDatabase
import com.prafull.algorithms.favourites.data.repo.FavRepoImpl
import com.prafull.algorithms.favourites.domain.FavRepo
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favModule = module {

    viewModel {
        FavouritesViewModel()
    }
    single<FavRepo> {
        FavRepoImpl()
    }
    single<FavAlgoDatabase> {
        Room.databaseBuilder(
            androidContext(), FavAlgoDatabase::class.java, "algo_db"
        ).build()
    }
    single<FavAlgoDao> {
        get<FavAlgoDatabase>().algoDao()
    }
}