package com.prafull.algorithms.settings

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.enrollToAi.enrollScreen.verifyApiKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel(
    context: Context
) : ViewModel(), KoinComponent {
    private val settingsRepository: SettingsRepo by inject()
    private val pref = context.getSharedPreferences("api_key", Context.MODE_PRIVATE)


    private val _keyAdded = MutableStateFlow(pref.getBoolean("isKeySaved", false))
    val keyAdded = _keyAdded.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    fun deleteFavourites() {
        viewModelScope.launch {
            settingsRepository.deleteAllFav()
        }
    }

    fun deleteSearchHistory() {
        viewModelScope.launch {
            settingsRepository.deleteSearchHistory()
        }
    }

    suspend fun changeApiKey(key: String, context: Context) {
        _loading.update { true }
        verifyApiKey(key).collectLatest {
            if (!it) {
                _loading.update { false }
                Toast.makeText(
                    context, "Invalid API Key", Toast.LENGTH_SHORT
                ).show()
                return@collectLatest
            }
            pref.apply {
                edit().putBoolean("isKeySaved", true).apply()
                edit().putString(Const.PREF_KEY, key).apply()
            }
            _loading.update { false }
            _keyAdded.update { true }
            Toast.makeText(
                context, "API Key added successfully", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun deleteApiKey() {
        viewModelScope.launch {
            pref.edit().putBoolean("isKeySaved", false).apply()
            pref.edit().putString(Const.PREF_KEY, "").apply()
            _keyAdded.update { false }
        }
    }
}