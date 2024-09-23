package com.prafull.algorithms.enrollToAi

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.prafull.algorithms.Routes
import com.prafull.algorithms.enrollToAi.enrollScreen.EnrollingScreen
import com.prafull.algorithms.enrollToAi.howToCreateApiKey.HowToCreateApiKeyScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.enrollToAi(navController: NavController) {
    navigation<Routes.EnrollToAiRoute>(startDestination = EnrollToAIRoutes.EnrollToAi) {
        composable<EnrollToAIRoutes.EnrollToAi> {
            EnrollingScreen(viewModel = koinViewModel(), navController)
        }
        composable<EnrollToAIRoutes.HowToCreateApiKey> {
            HowToCreateApiKeyScreen(navController = navController, viewModel = koinViewModel())
        }
    }
}

sealed interface EnrollToAIRoutes {


    @Serializable
    data object EnrollToAi : EnrollToAIRoutes

    @Serializable
    data object HowToCreateApiKey : EnrollToAIRoutes
}
