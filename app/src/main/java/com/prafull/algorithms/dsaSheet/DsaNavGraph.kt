package com.prafull.algorithms.dsaSheet

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.prafull.algorithms.Routes
import com.prafull.algorithms.dsaSheet.ui.DsaRevisionScreen
import com.prafull.algorithms.dsaSheet.ui.DsaSheetScreen
import com.prafull.algorithms.dsaSheet.ui.DsaSheetViewModel
import com.prafull.algorithms.dsaSheet.ui.DsaSolveQuestions
import com.prafull.algorithms.dsaSheet.ui.dsa_question_Screen.DsaQuestionScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.dsaScreen(navController: NavHostController) {
    navigation<Routes.DsaSheetRoutes>(startDestination = DsaSheetRoutes.DsaSheetScreen) {
        composable<DsaSheetRoutes.DsaSheetScreen> {
            DsaSheetScreen(viewModel = koinViewModel(), navController)
        }
        composable<DsaSheetRoutes.DsaRevisionScreen> {
            val dsaVm = getViewModel<DsaSheetViewModel>()
            dsaVm.getRevisionQuestions()
            DsaRevisionScreen(viewModel = dsaVm, navController = navController)
        }
        composable<DsaSheetRoutes.DsaSolvedQuestionScreen> {
            val dsaVm = getViewModel<DsaSheetViewModel>()
            dsaVm.getSolvedQuestions()
            DsaSolveQuestions(koinViewModel(), navController)
        }
        composable<DsaSheetRoutes.DsaQuestionScreen> {
            val args = it.toRoute<DsaSheetRoutes.DsaQuestionScreen>()
            DsaQuestionScreen(koinViewModel { parametersOf(args) }, navController)
        }
    }
}

sealed interface DsaSheetRoutes {
    @Serializable
    data object DsaSheetScreen : DsaSheetRoutes

    @Serializable
    data object DsaRevisionScreen : DsaSheetRoutes

    @Serializable
    data object DsaSolvedQuestionScreen : DsaSheetRoutes

    @Serializable
    data class DsaQuestionScreen(val topic: String, val question: String, val link: String) :
        DsaSheetRoutes
}

