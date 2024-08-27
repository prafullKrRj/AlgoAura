package com.prafull.algorithms

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.prafull.algorithms.data.local.AlgorithmEntity
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.screens.ai.AskAi
import com.prafull.algorithms.screens.ai.ChatViewModel
import com.prafull.algorithms.screens.code.CodeScreen
import com.prafull.algorithms.screens.code.CodeViewModel
import com.prafull.algorithms.screens.favourites.FavouriteCodeScreen
import com.prafull.algorithms.screens.favourites.FavouriteScreen
import com.prafull.algorithms.screens.folder.FolderScreen
import com.prafull.algorithms.screens.folder.FolderViewModel
import com.prafull.algorithms.screens.home.AlgoViewModel
import com.prafull.algorithms.screens.home.HomeScreen
import com.prafull.algorithms.screens.search.SearchScreen
import com.prafull.algorithms.screens.search.SearchViewModel
import com.prafull.algorithms.ui.theme.AlgorithmsTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(key1 = currentRoute) {
        Log.d("CurrentRoute", currentRoute.toString())
    }
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
            startDestination = Routes.Home
        ) {
            composable<Routes.Search> {
                SearchScreen(searchViewModel, navController)
            }
            navigation<Routes.Home>(startDestination = Routes.HomeScreen) {
                composable<Routes.HomeScreen> {
                    HomeScreen(viewModel, navController)
                }
                composable<Routes.FolderScreen> {
                    val path = it.toRoute<Routes.FolderScreen>()
                    val folderViewModel: FolderViewModel = koinViewModel()
                    folderViewModel.getFiles(path.path)
                    FolderScreen(folderViewModel, path, navController)
                }
            }
            composable<Routes.History> {
                FavouriteScreen(favouritesViewModel = getViewModel(), navController = navController)
            }
            composable<Routes.CodeScreen> {
                val path = it.toRoute<Routes.CodeScreen>()
                val codeViewModel: CodeViewModel = koinViewModel { parametersOf(path.toFileInfo()) }
                CodeScreen(viewModel = codeViewModel, navController)
            }
            composable<Routes.FavouriteCodeScreen> {
                val data = it.toRoute<Routes.FavouriteCodeScreen>()
                FavouriteCodeScreen(algo = data.toAlgorithmEntity(), koinViewModel(), navController)
            }
            composable<Routes.AskAi> {
                val data = it.toRoute<Routes.AskAi>()
                val chatViewModel: ChatViewModel = koinViewModel { parametersOf(data) }
                AskAi(chatViewModel, navController)
            }
            composable<Routes.SearchWeb> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    Text(
                        text = "Under Development 🚧", style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selected: Int, onClick: (Routes, Int) -> Unit) {

    NavigationBar(Modifier.fillMaxWidth()) {
        BottomBarItems().items.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                if (item.drawableIcon != null) {
                    Icon(
                        painter = painterResource(id = item.drawableIcon),
                        contentDescription = item.title
                    )
                } else {
                    Icon(
                        imageVector = if (item.selected) item.selectedIcon!! else item.unselectedIcon!!,
                        contentDescription = item.title
                    )
                }
            }, label = {
                Text(text = item.title)
            }, selected = index == selected, onClick = {
                onClick(item.route, index)
            })
        }
    }
}

@Immutable
data class BottomBarItems(
    val items: List<BottomBarItem> = listOf(
        BottomBarItem(
            title = "Home",
            selected = true,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Routes.HomeScreen
        ), BottomBarItem(
            title = "Search",
            selected = false,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = Routes.Search
        ), BottomBarItem(
            title = "Favourites",
            selected = false,
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            route = Routes.History
        ), BottomBarItem(
            title = "Search Web",
            selected = false,
            route = Routes.SearchWeb,
            drawableIcon = R.drawable.baseline_web_24
        )
    )
)

@Immutable
data class BottomBarItem(
    val title: String,
    val selected: Boolean,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val route: Routes,
    @DrawableRes val drawableIcon: Int? = null
)


sealed interface Routes {
    @Serializable
    data object HomeScreen : Routes

    @Serializable
    data class FolderScreen(
        val path: String, val name: String
    ) : Routes

    @Serializable
    data object Home : Routes


    @Serializable
    data object Search : Routes

    @Serializable
    data object History : Routes

    @Serializable
    data class CodeScreen(
        val id: String,
        val name: String,
        val path: String,
        val langName: String
    ) : Routes {
        fun toFileInfo() = FileInfo(
            id = id, name = name, path = path, language = ProgrammingLanguage.valueOf(langName)
        )

    }

    @Serializable
    data class AskAi(
        val code: String, val programName: String, val message: String, val language: String
    ) : Routes

    @Serializable
    data object SearchWeb : Routes

    @Serializable
    data class FavouriteCodeScreen(
        val id: String,
        val code: String,
        val language: String,
        val extension: String,
        val title: String = ""
    ) : Routes {
        fun toAlgorithmEntity(): AlgorithmEntity {
            return AlgorithmEntity(
                id = id, code = code, language = language, extension = extension, title = title
            )
        }
    }
}

fun canShowBottomBar(current: String): Boolean {
    return current != "com.prafull.algorithms.Routes.FolderScreen/{path}/{name}" && current != "com.prafull.algorithms.Routes.CodeScreen/{id}/{name}/{path}/{langName}" && current != "com.prafull.algorithms.Routes.FavouriteCodeScreen/{id}/{code}/{language}/{extension}?title={title}" && current != "com.prafull.algorithms.Routes.AskAi/{code}/{programName}/{message}/{language}"
}

fun NavController.goBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
