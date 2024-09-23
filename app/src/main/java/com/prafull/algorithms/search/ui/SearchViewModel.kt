package com.prafull.algorithms.search.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.models.ProgrammingLanguage
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.search.data.local.SearchedEntity
import com.prafull.algorithms.search.domain.SearchRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {

    private val searchRepo: SearchRepo by inject()

    var query by mutableStateOf("")

    var searchResults by mutableStateOf(emptyList<FileInfo>())
    var loading by mutableStateOf(false)
    var error by mutableStateOf("")

    var languages by mutableStateOf(emptyList<ProgrammingLanguage>())

    val searchedElements = searchRepo.getSearchedAlgorithms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var results by mutableStateOf(searchResults)

    fun filterResults(language: ProgrammingLanguage) {
        if (language == ProgrammingLanguage.UNKNOWN) {
            results = searchResults
            return
        }
        results = searchResults.filter {
            it.language == language
        }
    }

    fun search(searchedEntity: SearchedEntity) {
        if (query.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            searchRepo.insertSearchedText(
                searchedEntity.copy(
                    lastTime = System.currentTimeMillis()
                )
            )
            loading = true
            searchResults = emptyList()
            results = emptyList()
            error = ""
            searchRepo.getListOfDocuments(query.lowercase()).collectLatest { resp ->
                when (resp) {
                    is BaseClass.Success -> {
                        searchResults = resp.data
                        loading = false
                        languages = searchResults.map { it.language }.distinct()
                        results = searchResults
                    }

                    is BaseClass.Error -> {
                        resp.message.let {
                            error = it
                        }
                        loading = false
                    }

                    is BaseClass.Loading -> {
                        loading = true
                    }
                }
            }
        }
    }
}

sealed interface SearchState {
    data object Loading : SearchState
    data class Success(val data: List<FileInfo>) : SearchState
    data class Error(val message: String) : SearchState
    data object Initial : SearchState
}