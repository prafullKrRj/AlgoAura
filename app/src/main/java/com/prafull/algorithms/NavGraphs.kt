package com.prafull.algorithms

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.prafull.algorithms.features.complexSearch.ui.algoScreen.ComplexLanguageAlgoVM
import com.prafull.algorithms.features.complexSearch.ui.algoScreen.LanguageAlgoScreen
import com.prafull.algorithms.features.complexSearch.ui.lang.ComplexLanguageData
import com.prafull.algorithms.features.complexSearch.ui.main.ComplexSearchMain
import com.prafull.algorithms.features.complexSearch.ui.main.ComplexSearchVM
import com.prafull.algorithms.features.complexSearch.ui.search.ComplexSearchScreen
import com.prafull.algorithms.features.complexSearch.ui.searchedAlgo.ComplexSearchResultScreen
import com.prafull.algorithms.features.dsaSheet.DsaRevisionScreen
import com.prafull.algorithms.features.dsaSheet.DsaSheetScreen
import com.prafull.algorithms.features.dsaSheet.DsaSheetViewModel
import com.prafull.algorithms.features.enrollToAi.EnrollingScreen
import com.prafull.algorithms.features.enrollToAi.howToCreateApiKey.HowToCreateApiKeyScreen
import com.prafull.algorithms.features.favourites.FavouriteCodeScreen
import com.prafull.algorithms.features.favourites.FavouriteScreen
import com.prafull.algorithms.features.homeScreen.folder.FolderScreen
import com.prafull.algorithms.features.homeScreen.folder.FolderViewModel
import com.prafull.algorithms.features.homeScreen.home.AlgoViewModel
import com.prafull.algorithms.features.homeScreen.home.HomeScreen
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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

fun NavGraphBuilder.homeNav(homeViewModel: AlgoViewModel, navController: NavController) {
    navigation<Routes.HomeRoutes>(startDestination = HomeRoutes.HomeScreen) {
        composable<HomeRoutes.HomeScreen> {
            HomeScreen(homeViewModel, navController)
        }
        composable<HomeRoutes.FolderScreen> {
            val path = it.toRoute<HomeRoutes.FolderScreen>()
            val folderViewModel: FolderViewModel = koinViewModel()
            folderViewModel.getFiles(path.path)
            FolderScreen(folderViewModel, path, navController)
        }
    }
}

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
