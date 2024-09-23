package com.prafull.algorithms.dsaSheet.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val topicId: Long = 0,
    val topicName: String,
    val topicDetails: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = TopicEntity::class,
        parentColumns = ["topicId"],
        childColumns = ["topicOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val questionId: Long = 0,
    val name: String,
    val link: String,
    val solved: Boolean = false,
    val revision: Boolean = false,
    val note: String = "",
    val topicOwnerId: Long = 0// Foreign key referring to Topic
)

data class TopicWithQuestions(
    @Embedded val topic: TopicEntity,
    @Relation(
        parentColumn = "topicId",
        entityColumn = "topicOwnerId"
    )
    val questions: List<QuestionEntity>
)
