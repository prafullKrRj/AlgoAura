package com.prafull.algorithms

import android.app.Application
import com.prafull.algorithms.ai.aiModule
import com.prafull.algorithms.codeScreen.di.codeModule
import com.prafull.algorithms.complexSearch.di.complexSearchModule
import com.prafull.algorithms.dsaSheet.dsaSheetModule
import com.prafull.algorithms.enrollToAi.di.enrollAiModule
import com.prafull.algorithms.favourites.di.favModule
import com.prafull.algorithms.homeScreen.di.homeModule
import com.prafull.algorithms.search.di.searchModule
import com.prafull.algorithms.settings.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AlgorithmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlgorithmApp)
            androidLogger()
            modules(
                homeModule,
                searchModule,
                dsaSheetModule,
                favModule,
                codeModule,
                aiModule,
                enrollAiModule,
                complexSearchModule,
                settingsModule
            )
        }
    }
}
