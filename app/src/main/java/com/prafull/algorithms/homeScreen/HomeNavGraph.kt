package com.prafull.algorithms.homeScreen

import androidx.annotation.DrawableRes
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.prafull.algorithms.Routes
import com.prafull.algorithms.homeScreen.folder.FolderScreen
import com.prafull.algorithms.homeScreen.folder.FolderViewModel
import com.prafull.algorithms.homeScreen.home.AlgoViewModel
import com.prafull.algorithms.homeScreen.home.HomeScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


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

sealed interface HomeRoutes {
    @Serializable
    data class FolderScreen(
        val path: String, val name: String, @DrawableRes val langLogo: Int
    ) : HomeRoutes

    @Serializable
    data object HomeScreen : HomeRoutes
}