package com.prafull.algorithms.dsaSheet.ui.dsa_question_Screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.dsaSheet.DsaSheetRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


data class QuestionAbout(
    val topic: String,
    val questionName: String,
    val link: String,
    val solutionJava: String,
    val solutionCpp: String
)

class DsaQuestionViewModel(
    val about: DsaSheetRoutes.DsaQuestionScreen
) : ViewModel(), KoinComponent {
    private val context: Context by inject()
    fun isKeySaved() =
        context.getSharedPreferences(Const.API_KEY_PREF, Context.MODE_PRIVATE)
            .getBoolean("isKeySaved", false)

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

