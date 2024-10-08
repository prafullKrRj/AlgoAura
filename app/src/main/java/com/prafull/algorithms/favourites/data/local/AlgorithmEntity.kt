package com.prafull.algorithms.favourites.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prafull.algorithms.commons.models.Algorithm
import com.prafull.algorithms.commons.models.ProgrammingLanguage
import com.prafull.algorithms.favourites.FavouritesRoutes


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
