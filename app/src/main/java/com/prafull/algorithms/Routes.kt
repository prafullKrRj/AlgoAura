package com.prafull.algorithms

import com.prafull.algorithms.data.local.algo.AlgorithmEntity
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.utils.getLanguageFromString
import kotlinx.serialization.Serializable

sealed interface Routes {


    @Serializable
    data object HomeRoutes : Routes

    @Serializable
    data object Search : Routes

    @Serializable
    data object FavouriteRoutes : Routes


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
}

sealed interface FavouritesRoutes : Routes {

    @Serializable
    data object FavouriteScreen : Routes

    @Serializable
    data class FavouriteCodeScreen(
        val id: String,
        val code: String,
        val language: String,
        val extension: String,
        val title: String = ""
    ) : FavouritesRoutes {
        fun toAlgorithmEntity(): AlgorithmEntity {
            return AlgorithmEntity(
                id = id, code = code, language = language, extension = extension, title = title
            )
        }
    }
}

sealed interface DsaSheetRoutes : Routes {
    @Serializable
    data object DsaSheetScreen : DsaSheetRoutes

    @Serializable
    data object DsaRevisionScreen : DsaSheetRoutes
}

sealed interface EnrollToAIRoutes : Routes {


    @Serializable
    data object EnrollToAi : EnrollToAIRoutes

    @Serializable
    data object HowToCreateApiKey : EnrollToAIRoutes
}

sealed interface HomeRoutes : Routes {
    @Serializable
    data class FolderScreen(
        val path: String, val name: String
    ) : HomeRoutes

    @Serializable
    data object HomeScreen : HomeRoutes
}

sealed interface ComplexRoutes : Routes {
    @Serializable
    data object ComplexSearchMainScreen : ComplexRoutes

    @Serializable
    data object ComplexSearchScreen : ComplexRoutes

    @Serializable
    data class ComplexSearchResultScreen(
        val algoName: String
    ) : ComplexRoutes

    @Serializable
    data class ComplexSearchLanguage(
        val lang: String
    ) : ComplexRoutes


    /*
    *   To a particular algorithm from the particular language screen like from C++ to binary strings implementation of C++
    * */
    @Serializable
    data class ComplexLanguageAlgoRoute(
        val algo: String, val lang: String
    ) : ComplexRoutes {}
}