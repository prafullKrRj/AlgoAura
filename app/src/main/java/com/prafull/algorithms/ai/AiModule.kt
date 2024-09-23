package com.prafull.algorithms.ai

import android.content.Context
import com.prafull.algorithms.commons.utils.Const
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aiModule = module {
    viewModel {
        ChatViewModel(get(), get())
    }
    single<ApiKey> {
        val apiKey = runBlocking { fetchApiKey(androidContext()) }
        ApiKey(apiKey)
    }
}

suspend fun fetchApiKey(context: Context): String {
    val sharedPref = context.getSharedPreferences(Const.API_KEY_PREF, Context.MODE_PRIVATE)
    return sharedPref.getString(Const.PREF_KEY, "") ?: ""
}

data class ApiKey(
    val apiKey: String
)