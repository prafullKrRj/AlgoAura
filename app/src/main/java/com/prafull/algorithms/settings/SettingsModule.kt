package com.prafull.algorithms.settings

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {


    viewModel {
        SettingsViewModel()
    }
    single {
        SettingsRepo()
    }
}