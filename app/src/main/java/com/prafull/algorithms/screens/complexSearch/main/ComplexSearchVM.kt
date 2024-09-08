package com.prafull.algorithms.screens.complexSearch.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplexSearchVM : ViewModel(), KoinComponent {
    private val firestore by inject<FirebaseHelper>()
    var searchQuery by mutableStateOf("")
    private val _uiState = MutableStateFlow<ComplexSearchUiState>(ComplexSearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _langs = MutableStateFlow<BaseClass<List<String>>>(BaseClass.Loading)
    val langs = _langs.asStateFlow()

    init {
        getComplexLanguagesList()
    }

    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    loading = true,
                    searchResults = emptyList(),
                    error = Pair(false, Exception())
                )
            }
            firestore.getComplexSearchResults(searchQuery).collectLatest { response ->
                when (response) {
                    is BaseClass.Error -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                searchResults = emptyList(),
                                error = Pair(true, response.exception)
                            )
                        }
                    }

                    BaseClass.Loading -> {
                        _uiState.update {
                            it.copy(
                                loading = true,
                                searchResults = emptyList(),
                                error = Pair(false, Exception())
                            )
                        }
                    }

                    is BaseClass.Success -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                searchResults = response.data,
                                error = Pair(false, Exception())
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getComplexLanguagesList() {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.getComplexLanguages().collectLatest { response ->
                _langs.update {
                    response
                }
            }
        }
    }
}

data class ComplexSearchUiState(
    val loading: Boolean = false,
    val searchResults: List<String> = emptyList(),
    val error: Pair<Boolean, Exception> = Pair(false, Exception())
)