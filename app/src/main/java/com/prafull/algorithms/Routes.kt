package com.prafull.algorithms

import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.utils.getLanguageFromString
import kotlinx.serialization.Serializable

sealed interface Routes {


    @Serializable
    data object HomeRoutes : Routes

    @Serializable
    data object Search : Routes

    @Serializable
    data object FavouriteRoutes : Routes

    @Serializable
    data object SettingsRoute : Routes

    @Serializable
    data class CodeScreen(
        val id: String, val name: String, val path: String, val langName: String
    ) : Routes {
        fun toFileInfo(): FileInfo {
            return FileInfo(
                id = id, name = name, path = path, language = getLanguageFromString(langName)
            )
        }
    }

    @Serializable
    data class AskAi(
        val code: String, val programName: String, val message: String, val language: String
    ) : Routes

    @Serializable
    data object EnrollToAiRoute : Routes

    @Serializable
    data object DsaSheetRoutes : Routes

    @Serializable
    data object ComplexScreens : Routes

    @Serializable
    data object Libraries : Routes
}
