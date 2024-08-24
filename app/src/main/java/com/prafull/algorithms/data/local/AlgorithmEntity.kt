package com.prafull.algorithms.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prafull.algorithms.Routes
import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.models.ProgrammingLanguage


@Entity
data class AlgorithmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val language: String,
    val extension: String,
    val title: String = "",
    val time: Long = System.currentTimeMillis()
) {
    fun toAlgorithms(): Algorithm {
        return Algorithm(
            code = code,
            language = ProgrammingLanguage.valueOf(language),
            langName = ProgrammingLanguage.valueOf(language).fileName,
            extension = extension,
            title = title
        )
    }

    fun toFavouriteCodeScreen(): Routes.FavouriteCodeScreen {
        return Routes.FavouriteCodeScreen(
            id = id,
            code = code,
            language = language,
            extension = extension,
            title = title
        )
    }
}
