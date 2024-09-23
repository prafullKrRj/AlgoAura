package com.prafull.algorithms.homeScreen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.commons.models.FolderInfo
import com.prafull.algorithms.commons.models.ProgrammingLanguage
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.homeScreen.domain.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlgoViewModel : ViewModel(), KoinComponent {
    private val homeRepo: HomeRepo by inject()

    private val _state = MutableStateFlow<BaseClass<List<FolderInfo>>>(BaseClass.Loading)
    val state = _state.asStateFlow()


    private val _cachedData = hashMapOf<ProgrammingLanguage, List<FolderInfo>>()


    fun getFromLanguage(language: ProgrammingLanguage) {
        if (_cachedData.containsKey(language)) {
            _state.update {
                BaseClass.Success(_cachedData[language]!!)
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                BaseClass.Loading
            }
            homeRepo.getAlgoGroups(language).collect { resp ->
                _state.update {
                    resp
                }
                if (resp is BaseClass.Success) {
                    _cachedData[language] = resp.data
                }
            }
        }
    }
}