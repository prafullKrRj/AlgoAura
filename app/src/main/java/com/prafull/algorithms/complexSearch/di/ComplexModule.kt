package com.prafull.algorithms.complexSearch.di

import com.prafull.algorithms.complexSearch.data.ComplexRepoImpl
import com.prafull.algorithms.complexSearch.domain.repo.ComplexSearchRepo
import com.prafull.algorithms.complexSearch.ui.lang.ComplexLanguageViewModel
import com.prafull.algorithms.complexSearch.ui.langAlgoScreen.ComplexLanguageAlgoVM
import com.prafull.algorithms.complexSearch.ui.main.ComplexSearchVM
import com.prafull.algorithms.complexSearch.ui.searchedAlgo.ComplexSearchAlgoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val complexSearchModule = module {

    viewModel { ComplexSearchVM() }
    viewModel { ComplexSearchAlgoVM(get()) }
    viewModel { ComplexLanguageViewModel(get()) }
    viewModel { ComplexLanguageAlgoVM(get()) }
    single<ComplexSearchRepo> {
        ComplexRepoImpl()
    }
}