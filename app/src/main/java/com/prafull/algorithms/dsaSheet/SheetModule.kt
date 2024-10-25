package com.prafull.algorithms.dsaSheet

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.prafull.algorithms.dsaSheet.data.local.DSASheetDB
import com.prafull.algorithms.dsaSheet.data.local.DSASheetDao
import com.prafull.algorithms.dsaSheet.data.local.leetcodeDSATopics
import com.prafull.algorithms.dsaSheet.data.repo.DsaSheetRepo
import com.prafull.algorithms.dsaSheet.ui.DsaSheetViewModel
import com.prafull.algorithms.dsaSheet.ui.dsa_question_Screen.DsaQuestionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val DSA_SHEET_DB = "dsa_sheet_db"
val dsaSheetModule = module {
    single<DSASheetDB> {
        Room.databaseBuilder(get(), DSASheetDB::class.java, DSA_SHEET_DB)
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
    viewModel {
        DsaSheetViewModel()
    }
    viewModel {
        DsaQuestionViewModel(get())
    }
    single {
        DsaSheetRepo()
    }
}