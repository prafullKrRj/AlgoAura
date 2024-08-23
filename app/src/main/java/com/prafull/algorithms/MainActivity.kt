package com.prafull.algorithms

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.FirebaseApp
import com.prafull.algorithms.screens.code.CodeScreen
import com.prafull.algorithms.screens.code.CodeViewModel
import com.prafull.algorithms.screens.folder.FolderScreen
import com.prafull.algorithms.screens.folder.FolderViewModel
import com.prafull.algorithms.screens.history.HistoryScreen
import com.prafull.algorithms.screens.home.AlgoViewModel
import com.prafull.algorithms.screens.home.HomeScreen
import com.prafull.algorithms.screens.search.SearchScreen
import com.prafull.algorithms.ui.theme.AlgorithmsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
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
    val viewModel: AlgoViewModel = viewModel()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(key1 = currentRoute) {
        Log.d("CurrentRoute", currentRoute.toString())
    }
    Scaffold(modifier = Modifier.systemBarsPadding(), bottomBar = {
        if (canShowBottomBar(currentRoute.toString())) {
            BottomNavigationBar {
                navController.popBackStack()
                navController.navigate(it)
            }
        }
    }) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Routes.Home
        ) {
            composable<Routes.Search> {
                SearchScreen()
            }
            navigation<Routes.Home>(startDestination = Routes.HomeScreen) {
                composable<Routes.HomeScreen> {
                    HomeScreen(viewModel, navController)
                }
                composable<Routes.FolderScreen> {
                    val path = it.toRoute<Routes.FolderScreen>()
                    val folderViewModel: FolderViewModel = viewModel()
                    folderViewModel.getFies(path.path)
                    FolderScreen(folderViewModel, path, navController)
                }
            }
            composable<Routes.History> {
                HistoryScreen()
            }
            composable<Routes.CodeScreen> {
                val path = it.toRoute<Routes.CodeScreen>()
                val codeViewModel: CodeViewModel = viewModel()

                CodeScreen(viewModel = codeViewModel)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(onClick: (Routes) -> Unit) {
    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(Modifier.fillMaxWidth()) {
        BottomBarItems().items.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                Icon(
                    imageVector = if (item.selected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = null
                )
            }, label = {
                Text(text = item.title)
            }, selected = index == selected.intValue, onClick = {
                selected.intValue = index
                onClick(item.route)
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
            title = "History",
            selected = false,
            selectedIcon = Icons.Filled.Refresh,
            unselectedIcon = Icons.Outlined.Refresh,
            route = Routes.History
        )
    )
)

@Immutable
data class BottomBarItem(
    val title: String,
    val selected: Boolean,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Routes
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
        val path: String
    ) : Routes

    @Serializable
    data object ChatBot : Routes
}

fun canShowBottomBar(current: String): Boolean {
    return current != "com.prafull.algorithms.Routes.FolderScreen/{path}/{name}"
            && current != "com.prafull.algorithms.Routes.CodeScreen"
            && current != "com.prafull.algorithms.Routes.ChatBot"
}

fun NavController.goBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
