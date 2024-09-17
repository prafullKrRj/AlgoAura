package com.prafull.algorithms.data.local.questions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DSASheetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Transaction
    @Query("SELECT * FROM TopicEntity WHERE topicId = :topicId")
    fun getTopicWithQuestions(topicId: Long): Flow<TopicWithQuestions>

    @Query("SELECT * FROM TopicEntity")
    fun getAllTopics(): Flow<List<TopicEntity>>

    // get all topics with questions
    @Transaction
    @Query("SELECT * FROM TopicEntity")
    fun getAllTopicsWithQuestions(): Flow<List<TopicWithQuestions>>

    @Transaction
    suspend fun insertAll(map: List<Topic>) {
        map.forEach { topic ->
            val topicId = insertTopic(topic.toTopicEntity())
            val questions = topic.questions.map { question ->
                QuestionEntity(
                    name = question.name,
                    link = question.link,
                    solved = question.solved,
                    revision = question.revision,
                    note = question.note,
                    topicOwnerId = topicId
                )
            }
            insertQuestions(questions)
        }
    }

    suspend fun updateQuestion(copy: QuestionEntity) {
        insertQuestions(listOf(copy))
    }

    @Transaction
    @Query(
        """
    SELECT * FROM TopicEntity 
    WHERE topicId IN (
        SELECT DISTINCT topicOwnerId 
        FROM QuestionEntity 
        WHERE revision = 1
    )
"""
    )
    fun getRevisionQuestions(): Flow<List<TopicWithQuestions>>
}