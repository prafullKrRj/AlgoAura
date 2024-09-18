package com.prafull.algorithms.data.local.algo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prafull.algorithms.FavouritesRoutes
import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.models.ProgrammingLanguage


@Entity
data class AlgorithmEntity(
    @PrimaryKey
    val id: String,
    val code: String,
    val language: String,
    val extension: String,
    val title: String = "",
    val time: Long = System.currentTimeMillis()
) {
    fun toAlgorithms(): Algorithm {
        return Algorithm(
            id = id,
            code = code,
            language = ProgrammingLanguage.valueOf(language),
            langName = ProgrammingLanguage.valueOf(language).fileName,
            extension = extension,
            title = title
        )
    }

    fun toFavouriteCodeScreen(): FavouritesRoutes.FavouriteCodeScreen {
        return FavouritesRoutes.FavouriteCodeScreen(
            id = id,
            code = code,
            language = language,
            extension = extension,
            title = title
        )
    }
}
