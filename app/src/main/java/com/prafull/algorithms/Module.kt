package com.prafull.algorithms

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.data.firebase.FirebaseHelperImpl
import com.prafull.algorithms.data.gemini.GeminiRepo
import com.prafull.algorithms.data.gemini.GeminiRepoImpl
import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.data.local.AlgoDatabase
import com.prafull.algorithms.data.room.RoomHelper
import com.prafull.algorithms.data.room.RoomHelperImpl
import com.prafull.algorithms.screens.ai.ChatViewModel
import com.prafull.algorithms.screens.code.CodeViewModel
import com.prafull.algorithms.screens.favourites.FavouritesViewModel
import com.prafull.algorithms.screens.folder.FolderViewModel
import com.prafull.algorithms.screens.home.AlgoViewModel
import com.prafull.algorithms.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single<FirebaseHelper> {
        FirebaseHelperImpl(get(), get())
    }
    single<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }
    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance()
    }
    single<AlgoDatabase> {
        Room.databaseBuilder(
            androidContext(), AlgoDatabase::class.java, "algo_db"
        ).build()
    }
    single<AlgoDao> {
        get<AlgoDatabase>().algoDao()
    }
    single<RoomHelper> {
        RoomHelperImpl(get())
    }
    single<GeminiRepo> {
        GeminiRepoImpl()
    }
    viewModel {
        AlgoViewModel(get())
    }
    viewModel {
        CodeViewModel(get(), get())
    }
    viewModel {
        FolderViewModel(get())
    }
    viewModel { ChatViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavouritesViewModel(get()) }
}