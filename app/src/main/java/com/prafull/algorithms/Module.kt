package com.prafull.algorithms

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.data.firebase.FirebaseHelperImpl
import com.prafull.algorithms.data.local.AlgoDao
import com.prafull.algorithms.data.local.AlgoDatabase
import com.prafull.algorithms.data.room.RoomHelper
import com.prafull.algorithms.data.room.RoomHelperImpl
import com.prafull.algorithms.screens.ai.ChatViewModel
import com.prafull.algorithms.screens.code.CodeViewModel
import com.prafull.algorithms.screens.complexSearch.algoScreen.ComplexLanguageAlgoVM
import com.prafull.algorithms.screens.complexSearch.lang.ComplexLanguageViewModel
import com.prafull.algorithms.screens.complexSearch.main.ComplexSearchVM
import com.prafull.algorithms.screens.complexSearch.searchedAlgo.ComplexSearchAlgoVM
import com.prafull.algorithms.screens.favourites.FavouritesViewModel
import com.prafull.algorithms.screens.folder.FolderViewModel
import com.prafull.algorithms.screens.home.AlgoViewModel
import com.prafull.algorithms.screens.search.SearchViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
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
    single<ApiKey> {
        val firestore = get<FirebaseFirestore>()
        val apiKey = runBlocking { fetchApiKey(firestore) }
        ApiKey(apiKey)
    }
    single<AlgoDao> {
        get<AlgoDatabase>().algoDao()
    }
    single<RoomHelper> {
        RoomHelperImpl(get())
    }
    viewModel {
        AlgoViewModel()
    }
    viewModel {
        CodeViewModel(get())
    }
    viewModel {
        FolderViewModel(get())
    }
    viewModel { ChatViewModel(get(), get()) }
    viewModel { SearchViewModel() }
    viewModel { FavouritesViewModel() }
    viewModel { ComplexSearchVM() }
    viewModel { ComplexSearchAlgoVM(get()) }
    viewModel { ComplexLanguageViewModel(get()) }
    viewModel { ComplexLanguageAlgoVM(get()) }
}

suspend fun fetchApiKey(firestore: FirebaseFirestore): String {
    val doc = firestore.collection("key").document("apiKey").get().await()
    return doc.getString("key") ?: ""
}

data class ApiKey(
    val apiKey: String
)