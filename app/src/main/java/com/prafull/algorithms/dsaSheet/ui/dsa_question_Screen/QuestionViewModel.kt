package com.prafull.algorithms.dsaSheet.ui.dsa_question_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.dsaSheet.DsaSheetRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class QuestionAbout(
    val topic: String,
    val questionName: String,
    val link: String,
    val solutionJava: String,
    val solutionCpp: String
)

class DsaQuestionViewModel(
    val about: DsaSheetRoutes.DsaQuestionScreen
) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _state = MutableStateFlow<BaseClass<QuestionAbout>>(BaseClass.Loading)
    val state = _state.asStateFlow()

    init {
        getAlgoFromFirestore()
    }

    fun getAlgoFromFirestore() {
        _state.update { BaseClass.Loading }
        viewModelScope.launch {
            try {
                val result =
                    firestore.collection("dsa_sheet_solutions").document(about.topic)
                        .collection(about.question).document("solutions").get().await()
                val data = result.data
                val questionAbout = QuestionAbout(
                    topic = about.topic,
                    questionName = about.question,
                    link = about.link,
                    solutionJava = data?.getOrDefault("solutionJava", "").toString(),
                    solutionCpp = data?.getOrDefault("solutionCpp", "").toString()
                )
                _state.update { BaseClass.Success(questionAbout) }
            } catch (e: Exception) {
                _state.update {
                    BaseClass.Error(e.localizedMessage ?: "Some error occurred")
                }
            }
        }
    }
}

