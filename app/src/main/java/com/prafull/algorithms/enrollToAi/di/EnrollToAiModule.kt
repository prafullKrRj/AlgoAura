package com.prafull.algorithms.enrollToAi.di

import com.prafull.algorithms.enrollToAi.enrollScreen.ApiKeyViewModel
import com.prafull.algorithms.enrollToAi.howToCreateApiKey.HowToCreateApiKeyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val enrollAiModule = module {


    viewModel { ApiKeyViewModel(androidContext()) }
    viewModel { HowToCreateApiKeyViewModel() }
}