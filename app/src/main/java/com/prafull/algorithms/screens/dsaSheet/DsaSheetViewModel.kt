package com.prafull.algorithms.screens.dsaSheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DsaSheetViewModel : ViewModel(), KoinComponent {
    private val repo: DsaSheetRepo by inject()
    val topicWithQuestions = repo.getTopicsWithQuestions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {

    }
}