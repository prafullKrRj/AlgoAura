package com.prafull.algorithms.screens.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.AskAiChip
import com.prafull.algorithms.commons.CodeScreenBottomBar
import com.prafull.algorithms.commons.CodeScreenTopAppBar
import com.prafull.algorithms.data.local.AlgorithmEntity
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.getKodeViewLanguageFromLanguage
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    favouritesViewModel: FavouritesViewModel = hiltViewModel(), navController: NavController
) {
    val algos by favouritesViewModel.favouriteAlgorithms.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "⭐ Favourites")
        })
    }) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(algos, key = {
                it.id
            }) { algo ->
                FavouriteAlgoCard(algo = algo, navController)
            }
        }
    }
}

@Composable
private fun FavouriteAlgoCard(algo: AlgorithmEntity, navController: NavController) {
    val algorithm = algo.toAlgorithms()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(algo.toFavouriteCodeScreen())
            }
            .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(.85f)
            ) {
                Text(text = algorithm.title, style = MaterialTheme.typography.labelMedium)
            }
            Image(
                painter = painterResource(id = algorithm.language.logo),
                contentDescription = null,
                modifier = Modifier
                    .weight(.15f)
                    .alpha(1f)
            )
        }
    }
}

@Composable
fun FavouriteCodeScreen(
    algo: AlgorithmEntity, viewModel: FavouritesViewModel, navController: NavController
) {
    val onFavClick = remember {
        {
            viewModel.toggle(algo)
        }
    }
    Scaffold(topBar = {
        CodeScreenTopAppBar(
            isFavorite = true,
            onFavClick = onFavClick,
            onBackClick = navController::goBackStack,
            title = algo.title,
            showToggle = true
        )
    }, bottomBar = {
        CodeScreenBottomBar(algo.code)
    }, floatingActionButton = {
        AskAiChip {
            navController.navigate(Routes.AskAi)
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            val highlights = remember {
                Highlights.Builder(code = algo.code).theme(SyntaxThemes.darcula()).language(
                    language = getKodeViewLanguageFromLanguage(
                        ProgrammingLanguage.valueOf(
                            algo.language
                        )
                    )
                ).build()
            }
            CodeTextView(highlights = highlights)
        }
    }
}