package com.prafull.algorithms

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.data.firebase.FirebaseHelperImpl
import com.prafull.algorithms.data.local.algo.AlgoDao
import com.prafull.algorithms.data.local.algo.AlgoDatabase
import com.prafull.algorithms.data.local.questions.DSASheetDB
import com.prafull.algorithms.data.local.questions.DSASheetDao
import com.prafull.algorithms.data.local.questions.leetcodeDSATopics
import com.prafull.algorithms.data.room.RoomHelper
import com.prafull.algorithms.data.room.RoomHelperImpl
import com.prafull.algorithms.screens.ai.ChatViewModel
import com.prafull.algorithms.screens.code.CodeViewModel
import com.prafull.algorithms.screens.complexSearch.algoScreen.ComplexLanguageAlgoVM
import com.prafull.algorithms.screens.complexSearch.lang.ComplexLanguageViewModel
import com.prafull.algorithms.screens.complexSearch.main.ComplexSearchVM
import com.prafull.algorithms.screens.complexSearch.searchedAlgo.ComplexSearchAlgoVM
import com.prafull.algorithms.screens.dsaSheet.DsaSheetRepo
import com.prafull.algorithms.screens.dsaSheet.DsaSheetViewModel
import com.prafull.algorithms.screens.enrollToAi.ApiKeyViewModel
import com.prafull.algorithms.screens.enrollToAi.howToCreateApiKey.HowToCreateApiKeyViewModel
import com.prafull.algorithms.screens.favourites.FavouritesViewModel
import com.prafull.algorithms.screens.folder.FolderViewModel
import com.prafull.algorithms.screens.home.AlgoViewModel
import com.prafull.algorithms.screens.search.SearchViewModel
import com.prafull.algorithms.utils.Const
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


@DelicateCoroutinesApi
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
    single<DSASheetDB> {
        Room.databaseBuilder(get(), DSASheetDB::class.java, "dsa_sheet_db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                        get<DSASheetDB>().dsasheetDao().insertAll(leetcodeDSATopics)
                    }
                }
            }).build()
    }
    single<DSASheetDao> {
        get<DSASheetDB>().dsasheetDao()
    }
    single<ApiKey> {
        val apiKey = runBlocking { fetchApiKey(androidContext()) }
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
    viewModel { ApiKeyViewModel(androidContext()) }
    viewModel { HowToCreateApiKeyViewModel() }
    viewModel { DsaSheetViewModel() }
    single { DsaSheetRepo() }

}

suspend fun fetchApiKey(context: Context): String {
    val sharedPref = context.getSharedPreferences(Const.API_KEY_PREF, Context.MODE_PRIVATE)
    return sharedPref.getString(Const.PREF_KEY, "") ?: ""
}

data class ApiKey(
    val apiKey: String
)