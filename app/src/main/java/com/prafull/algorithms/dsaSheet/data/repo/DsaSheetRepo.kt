package com.prafull.algorithms.dsaSheet.data.repo

import com.prafull.algorithms.dsaSheet.data.local.DSASheetDao
import com.prafull.algorithms.dsaSheet.data.local.QuestionEntity
import com.prafull.algorithms.dsaSheet.data.local.TopicWithQuestions
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

    fun getSolvedQuestions(): Flow<List<TopicWithQuestions>> {
        return dao.getSolvedQuestions()
    }
}