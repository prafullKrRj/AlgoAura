package com.prafull.algorithms.features.dsaSheet

import com.prafull.algorithms.data.local.questions.DSASheetDao
import com.prafull.algorithms.data.local.questions.QuestionEntity
import com.prafull.algorithms.data.local.questions.TopicWithQuestions
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DsaSheetRepo : KoinComponent {

    private val dao: DSASheetDao by inject()

    fun getTopicsWithQuestions(): Flow<List<TopicWithQuestions>> {
        return dao.getAllTopicsWithQuestions()
    }

    suspend fun updateQuestion(question: QuestionEntity) {
        dao.updateQuestion(question)
    }

    fun getRevisionQuestions(): Flow<List<TopicWithQuestions>> {
        return dao.getRevisionQuestions()
    }
}