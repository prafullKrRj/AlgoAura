package com.prafull.algorithms.complexSearch

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.prafull.algorithms.Routes
import com.prafull.algorithms.complexSearch.ui.algoScreen.ComplexLanguageAlgoVM
import com.prafull.algorithms.complexSearch.ui.algoScreen.LanguageAlgoScreen
import com.prafull.algorithms.complexSearch.ui.lang.ComplexLanguageData
import com.prafull.algorithms.complexSearch.ui.main.ComplexSearchMain
import com.prafull.algorithms.complexSearch.ui.main.ComplexSearchVM
import com.prafull.algorithms.complexSearch.ui.search.ComplexSearchScreen
import com.prafull.algorithms.complexSearch.ui.searchedAlgo.ComplexSearchResultScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


fun NavGraphBuilder.complexNav(complexVM: ComplexSearchVM, navController: NavController) {
    navigation<Routes.ComplexScreens>(
        startDestination = ComplexRoutes.ComplexSearchMainScreen
    ) {
        composable<ComplexRoutes.ComplexSearchMainScreen> {
            ComplexSearchMain(viewModel = complexVM, navController = navController)
        }
        composable<ComplexRoutes.ComplexSearchLanguage> {
            val data = it.toRoute<ComplexRoutes.ComplexSearchLanguage>()
            ComplexLanguageData(
                viewModel = koinViewModel { parametersOf(data.lang) }, navController
            )
        }
        composable<ComplexRoutes.ComplexSearchScreen> {
            ComplexSearchScreen(complexVm = complexVM, navController)
        }
        composable<ComplexRoutes.ComplexSearchResultScreen> {
            val data = it.toRoute<ComplexRoutes.ComplexSearchResultScreen>()
            ComplexSearchResultScreen(
                viewModel = koinViewModel { parametersOf(data.algoName) }, navController
            )
        }
        composable<ComplexRoutes.ComplexLanguageAlgoRoute> {
            val data = it.toRoute<ComplexRoutes.ComplexLanguageAlgoRoute>()
            Log.d("Bugger", data.toString())
            val vm: ComplexLanguageAlgoVM = koinViewModel { parametersOf(data) }
            LanguageAlgoScreen(
                viewModel = vm, navController = navController
            )
        }
    }
}

sealed interface ComplexRoutes {
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