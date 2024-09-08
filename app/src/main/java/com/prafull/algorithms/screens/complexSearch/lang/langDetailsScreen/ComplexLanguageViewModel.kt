package com.prafull.algorithms.screens.complexSearch.lang.langDetailsScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.models.ComplexLanguageData
import com.prafull.algorithms.models.ComplexLanguageFiles
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplexLanguageViewModel(
    private val lang: String
) : ViewModel(), KoinComponent {
    private val firebase by inject<FirebaseHelper>()
    var langName: String by mutableStateOf("")

    private val _state = MutableStateFlow<BaseClass<ComplexLanguageData>>(BaseClass.Loading)
    val state = _state.asStateFlow()

    private val allProblems = MutableStateFlow<List<ComplexLanguageFiles>>(emptyList())
    private val _searchedProblems = MutableStateFlow<List<ComplexLanguageFiles>>(emptyList())
    val searchedProblems = _searchedProblems.asStateFlow()

    init {
        langName = lang
        getLangData()
    }

    private fun getLangData() {
        viewModelScope.launch(Dispatchers.IO) {
            firebase.getComplexLanguageData(lang).collectLatest { response ->
                _state.update { response }
                if (response is BaseClass.Success) {
                    allProblems.update {
                        response.data.files
                    }
                    _searchedProblems.update {
                        response.data.files
                    }
                }
            }
        }
    }

    fun filterProblems(query: String) {
        if (query == "") {
            _searchedProblems.update { allProblems.value }
            return
        }
        viewModelScope.launch {
            _searchedProblems.update {
                allProblems.value.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
    }


}