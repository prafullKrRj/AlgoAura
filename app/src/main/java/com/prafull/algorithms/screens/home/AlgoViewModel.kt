package com.prafull.algorithms.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.FirebaseHelper
import com.prafull.algorithms.data.RoomHelper
import com.prafull.algorithms.models.Algorithms
import com.prafull.algorithms.models.FolderInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AlgoViewModel : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow<BaseClass<List<FolderInfo>>>(BaseClass.Loading)
    val state = _state.asStateFlow()

    fun getFromLanguage(language: ProgrammingLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                BaseClass.Loading
            }
            FirebaseHelper.getFromLanguage(language).collect { resp ->
                _state.update {
                    resp
                }
            }
        }
    }

    fun saveAlgo(algo: Algorithms) = viewModelScope.launch {
        RoomHelper.insert(algo)
    }

    fun deleteAlgo(algo: Algorithms) = viewModelScope.launch {
        RoomHelper.delete(algo)
    }
}