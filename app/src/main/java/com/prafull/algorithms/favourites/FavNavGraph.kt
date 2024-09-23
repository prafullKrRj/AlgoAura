package com.prafull.algorithms.favourites

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.prafull.algorithms.Routes
import com.prafull.algorithms.favourites.data.local.AlgorithmEntity
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.favScreen(navController: NavController) {
    navigation<Routes.FavouriteRoutes>(startDestination = FavouritesRoutes.FavouriteScreen) {
        composable<FavouritesRoutes.FavouriteScreen> {
            FavouriteScreen(favouritesViewModel = getViewModel(), navController = navController)
        }
        composable<FavouritesRoutes.FavouriteCodeScreen> {
            val data = it.toRoute<FavouritesRoutes.FavouriteCodeScreen>()
            FavouriteCodeScreen(algo = data.toAlgorithmEntity(), koinViewModel(), navController)
        }
    }
}

sealed interface FavouritesRoutes {

    @Serializable
    data object FavouriteScreen : FavouritesRoutes

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



