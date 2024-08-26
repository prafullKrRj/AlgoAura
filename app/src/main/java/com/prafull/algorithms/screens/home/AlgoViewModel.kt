package com.prafull.algorithms.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.models.FolderInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlgoViewModel(
    private val firebaseHelper: FirebaseHelper
) : ViewModel() {
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
            firebaseHelper.getAlgoGroups(language).collect { resp ->
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