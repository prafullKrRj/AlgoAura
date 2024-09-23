package com.prafull.algorithms.dsaSheet

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.prafull.algorithms.Routes
import com.prafull.algorithms.dsaSheet.ui.DsaRevisionScreen
import com.prafull.algorithms.dsaSheet.ui.DsaSheetScreen
import com.prafull.algorithms.dsaSheet.ui.DsaSheetViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.dsaScreen(navController: NavController) {
    navigation<Routes.DsaSheetRoutes>(startDestination = DsaSheetRoutes.DsaSheetScreen) {
        composable<DsaSheetRoutes.DsaSheetScreen> {
            DsaSheetScreen(viewModel = koinViewModel(), navController)
        }
        composable<DsaSheetRoutes.DsaRevisionScreen> {
            val dsaVm = getViewModel<DsaSheetViewModel>()
            dsaVm.getRevisionQuestions()
            DsaRevisionScreen(viewModel = dsaVm, navController = navController)
        }
    }
}

sealed interface DsaSheetRoutes {
    @Serializable
    data object DsaSheetScreen : DsaSheetRoutes

    @Serializable
    data object DsaRevisionScreen : DsaSheetRoutes
}

