package com.prafull.algorithms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.prafull.algorithms.ai.AskAi
import com.prafull.algorithms.ai.ChatViewModel
import com.prafull.algorithms.codeScreen.ui.CodeScreen
import com.prafull.algorithms.codeScreen.ui.CodeViewModel
import com.prafull.algorithms.commons.ui.theme.AlgorithmsTheme
import com.prafull.algorithms.complexSearch.complexNav
import com.prafull.algorithms.complexSearch.ui.main.ComplexSearchVM
import com.prafull.algorithms.dsaSheet.dsaScreen
import com.prafull.algorithms.enrollToAi.enrollToAi
import com.prafull.algorithms.favourites.favScreen
import com.prafull.algorithms.homeScreen.home.AlgoViewModel
import com.prafull.algorithms.homeScreen.homeNav
import com.prafull.algorithms.search.ui.SearchScreen
import com.prafull.algorithms.search.ui.SearchViewModel
import com.prafull.algorithms.settings.SettingsScreen
import com.prafull.algorithms.settings.libraries.LibraryScreen
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        MobileAds.initialize(this)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        setContent {
            AlgorithmsTheme {

                App()
            }
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController()
    val viewModel: AlgoViewModel = koinViewModel()
    val searchViewModel: SearchViewModel = koinViewModel()
    val complexVM: ComplexSearchVM = koinViewModel()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(modifier = Modifier.systemBarsPadding(), bottomBar = {
        if (canShowBottomBar(currentRoute.toString())) {
            BottomNavigationBar(selected.intValue) { route, index ->
                navController.popBackStack()
                navController.navigate(route)
                selected.intValue = index
            }
        }
    }) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Routes.HomeRoutes
        ) {

            homeNav(viewModel, navController)
            complexNav(complexVM, navController)
            enrollToAi(navController)
            dsaScreen(navController)
            favScreen(navController)

            composable<Routes.Search> {
                SearchScreen(searchViewModel, navController)
            }
            composable<Routes.CodeScreen> {
                val path = it.toRoute<Routes.CodeScreen>()
                val codeViewModel: CodeViewModel = koinViewModel { parametersOf(path) }
                CodeScreen(viewModel = codeViewModel, navController)
            }
            composable<Routes.AskAi> {
                val data = it.toRoute<Routes.AskAi>()
                val chatViewModel: ChatViewModel = koinViewModel { parametersOf(data) }
                AskAi(chatViewModel, navController)
            }
            composable<Routes.SettingsRoute> {
                SettingsScreen(getViewModel(), navController)
            }
            composable<Routes.Libraries> {
                LibraryScreen(navController = navController)
            }
        }
    }
}


enum class Screens(
    val title: String,
    val route: Routes,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    HOME(
        "Home", Routes.HomeRoutes, R.drawable.baseline_home_24, R.drawable.outline_home_24
    ),
    SEARCH(
        "Search", Routes.Search, R.drawable.baseline_search_24, R.drawable.baseline_search_24
    ),
    DSA_SHEET(
        "DSA Sheet",
        Routes.DsaSheetRoutes,
        R.drawable.baseline_checklist_24,
        R.drawable.baseline_checklist_24
    ),
    FAVOURITES(
        "Favourites",
        Routes.FavouriteRoutes,
        R.drawable.baseline_favorite_24,
        R.drawable.baseline_favorite_border_24
    ),
    COMPLEX_SEARCH(
        "Complex Search",
        Routes.ComplexScreens,
        R.drawable.baseline_web_24,
        R.drawable.baseline_web_24
    )
}

@Composable
fun BottomNavigationBar(selected: Int, onClick: (Routes, Int) -> Unit) {
    NavigationBar(Modifier.fillMaxWidth()) {
        Screens.entries.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                Icon(
                    painter = painterResource(id = if (selected == index) item.selectedIcon else item.unselectedIcon),
                    contentDescription = item.title
                )
            }, label = {
                Text(text = item.title)
            }, selected = index == selected, onClick = {
                onClick(item.route, index)
            })
        }
    }
}

fun canShowBottomBar(current: String): Boolean {
    return current != "com.prafull.algorithms.homeScreen.HomeRoutes.FolderScreen/{path}/{name}/{langLogo}" && current != "com.prafull.algorithms.Routes.CodeScreen/{id}/{name}/{path}/{langName}" && current != "com.prafull.algorithms.favourites.FavouritesRoutes.FavouriteCodeScreen/{id}/{code}/{language}/{extension}?title={title}" && current != "com.prafull.algorithms.Routes.AskAi/{code}/{programName}/{message}/{language}" && current != "com.prafull.algorithms.complexSearch.ComplexRoutes.ComplexSearchLanguage/{lang}" && current != "com.prafull.algorithms.complexSearch.ComplexRoutes.ComplexSearchScreen" && current != "com.prafull.algorithms.complexSearch.ComplexRoutes.ComplexSearchResultScreen/{algoName}" && current != "com.prafull.algorithms.complexSearch.ComplexRoutes.ComplexLanguageAlgoRoute/{algo}/{lang}" && current != "com.prafull.algorithms.enrollToAi.EnrollToAIRoutes.EnrollToAi" && current != "com.prafull.algorithms.enrollToAi.EnrollToAIRoutes.HowToCreateApiKey" && current != "com.prafull.algorithms.dsaSheet.DsaSheetRoutes.DsaRevisionScreen" && current != "com.prafull.algorithms.Routes.SettingsRoute" && current != "com.prafull.algorithms.Routes.Libraries" && current != "com.prafull.algorithms.dsaSheet.DsaSheetRoutes.DsaQuestionScreen/{topic}/{question}/{link}"
}

fun NavController.goBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
