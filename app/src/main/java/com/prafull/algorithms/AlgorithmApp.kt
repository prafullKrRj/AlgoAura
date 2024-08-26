package com.prafull.algorithms

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AlgorithmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlgorithmApp)
            androidLogger()
            modules(appModule)
        }
    }
}
