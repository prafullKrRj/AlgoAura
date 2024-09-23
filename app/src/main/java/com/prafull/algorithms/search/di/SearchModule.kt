package com.prafull.algorithms.search.di

import androidx.room.Room
import com.prafull.algorithms.search.data.SearchRepoImpl
import com.prafull.algorithms.search.data.local.SearchDao
import com.prafull.algorithms.search.data.local.SearchDatabase
import com.prafull.algorithms.search.domain.SearchRepo
import com.prafull.algorithms.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val SEARCH_DATABASE = "search_database"

val searchModule = module {
    single<SearchDatabase> {
        Room.databaseBuilder(androidContext(), SearchDatabase::class.java, SEARCH_DATABASE)
            .build()
    }
    single<SearchDao> { get<SearchDatabase>().searchDao() }
    single<SearchRepo> { SearchRepoImpl() }
    viewModel<SearchViewModel> {
        SearchViewModel()
    }
}