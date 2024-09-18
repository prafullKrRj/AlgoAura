package com.prafull.algorithms.features.dsaSheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.local.questions.QuestionEntity
import com.prafull.algorithms.data.local.questions.TopicWithQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DsaSheetViewModel : ViewModel(), KoinComponent {


    fun onSolvedChanged(question: QuestionEntity, checked: Boolean) = viewModelScope.launch {
        repo.updateQuestion(question.copy(solved = checked))
    }

    fun onRevisionChanged(question: QuestionEntity, checked: Boolean) = viewModelScope.launch {
        repo.updateQuestion(question.copy(revision = checked))
    }

    fun onNoteAdded(question: QuestionEntity, note: String) = viewModelScope.launch {
        repo.updateQuestion(question.copy(note = note))
    }

    private val repo: DsaSheetRepo by inject()
    val topicWithQuestions = repo.getTopicsWithQuestions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    private val _revisionQuestion = MutableStateFlow<List<TopicWithQuestions>>(emptyList())
    val revisionQuestion = _revisionQuestion.asStateFlow()

    fun getRevisionQuestions() = viewModelScope.launch {
        repo.getRevisionQuestions().collect { res ->
            _revisionQuestion.update {
                res
            }
        }
    }
}