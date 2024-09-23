package com.prafull.algorithms.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {
    private val settingsRepository: SettingsRepo by inject()


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

}