package com.prafull.algorithms.settings

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {


    viewModel {
        SettingsViewModel(androidContext())
    }
    single {
        SettingsRepo()
    }
}