package com.prafull.algorithms.screens.searchWeb

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

class ComplexSearchViewModel : ViewModel(), KoinComponent {
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
    val isSearching: Boolean = false,
    val loading: Boolean = false,
    val languages: Any = Any(),
    val searchResults: Any = Any(),
    val error: Pair<Boolean, Exception> = Pair(false, Exception())
)