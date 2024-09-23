package com.prafull.algorithms.codeScreen.di

import com.prafull.algorithms.codeScreen.data.CodeRepoImpl
import com.prafull.algorithms.codeScreen.domain.CodeRepository
import com.prafull.algorithms.codeScreen.ui.CodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val codeModule = module {
    viewModel<CodeViewModel> {
        CodeViewModel(get())
    }
    single<CodeRepository> {
        CodeRepoImpl()
    }
}