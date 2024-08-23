package com.prafull.algorithms.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prafull.algorithms.models.Algorithms
import com.prafull.algorithms.models.ProgrammingLanguage


@Entity
data class AlgorithmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val language: String,
    val extension: String,
    val title: String = ""
) {
    fun toAlgorithms(): Algorithms {
        return Algorithms(
            code = code,
            language = ProgrammingLanguage.valueOf(language),
            langName = ProgrammingLanguage.valueOf(language).fileName,
            extension = extension,
            title = title
        )
    }
}
