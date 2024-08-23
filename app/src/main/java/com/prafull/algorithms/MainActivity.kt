package com.prafull.algorithms

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.FirebaseApp
import com.prafull.algorithms.ui.theme.AlgorithmsTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            AlgorithmsTheme {
                val navController = rememberNavController()
                val viewModel: AlgoViewModel = viewModel()
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                LaunchedEffect(key1 = currentRoute) {
                    Log.d("Route", "Current Route: $currentRoute")
                }
                Scaffold(modifier = Modifier.systemBarsPadding(), bottomBar = {
                    if (currentRoute.toString() != "com.prafull.algorithms.Routes.FolderScreen/{path}/{name}") {
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Green)
                            )
                        }
                        navigation<Routes.Home>(startDestination = Routes.HomeScreen) {
                            composable<Routes.HomeScreen> {
                                MainScreen(viewModel, navController)
                            }
                            composable<Routes.FolderScreen> {
                                val path = it.toRoute<Routes.FolderScreen>()
                                val folderViewModel: FolderViewModel = viewModel()
                                folderViewModel.getFies(path.path)
                                FolderScreen(folderViewModel, path, navController)
                            }
                        }
                        composable<Routes.History> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Red)
                            )
                        }
                        composable<Routes.CodeScreen> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Red)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    viewModel: FolderViewModel, folderScreen: Routes.FolderScreen, navController: NavController
) {
    val files by viewModel.files.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = folderScreen.name)
        }, navigationIcon = {
            IconButton(onClick = {
                navController.goBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "To Home"
                )
            }
        })
    }) { paddingValues ->
        when (files) {
            is BaseClass.Error -> {
                val message = (files as BaseClass.Error).message
                Text(text = message)
                Button(onClick = {
                    viewModel.getFies(folderScreen.path)
                }) {
                    Text(text = "Retry")
                }
            }

            BaseClass.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is BaseClass.Success -> {
                val data = (files as BaseClass.Success).data
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(data, key = {
                        "${it.path}-${it.name}"
                    }) { file ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        navController.navigate(
                                            Routes.CodeScreen
                                        )
                                    }
                                    .padding(8.dp)) {
                                Text(text = getFormattedName(getFileName(file.name)))
                            }
                        }
                    }
                }
            }
        }
    }

}

fun getFileName(name: String): String {
    return name.removeSuffix(".md").split(".").first()
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: AlgoViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }
    var switched by rememberSaveable {
        mutableStateOf(false)
    }
    if (!switched && state is BaseClass.Loading) {
        viewModel.getFromLanguage(ProgrammingLanguage.entries[selected.intValue])
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "AlgoAura")
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProgrammingLanguage.entries.filter {
                    it != ProgrammingLanguage.UNKNOWN
                }.forEachIndexed { index, programmingLanguage ->
                    FilterChip(selected = index == selected.intValue, onClick = {
                        selected.intValue = index
                        switched = true
                        viewModel.getFromLanguage(programmingLanguage)
                    }, label = {
                        Text(text = programmingLanguage.languageGenerics)
                    }, trailingIcon = {
                        Image(
                            painter = painterResource(id = programmingLanguage.logo),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    })
                }
            }
            when (state) {
                is BaseClass.Error -> {
                    val message = (state as BaseClass.Error).message
                    Text(text = message)
                    Button(onClick = {
                        viewModel.getFromLanguage(ProgrammingLanguage.entries[selected.intValue])
                    }) {
                        Text(text = "Retry")
                    }
                }

                BaseClass.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is BaseClass.Success -> {
                    val data = (state as BaseClass.Success).data
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(data, key = {
                            "${it.path}-${it.name}"
                        }) { folder ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                )
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            navController.navigate(
                                                Routes.FolderScreen(
                                                    path = folder.path, name = folder.name
                                                )
                                            )
                                        }
                                        .padding(8.dp)) {
                                    Text(text = getFormattedName(folder.name))
                                }
                            }
                        }
                    }
                }
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


fun getFormattedName(input: String): String {
    return input.replace(Regex("[_-]"), " ").split(" ")
        .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
}

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
    data object CodeScreen : Routes

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
