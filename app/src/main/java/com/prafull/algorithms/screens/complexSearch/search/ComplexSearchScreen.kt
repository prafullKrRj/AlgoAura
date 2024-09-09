package com.prafull.algorithms.screens.complexSearch.search


/**
 *        ComplexSearchScreen
 *         This is the UI for the complex search screen
 *         It contains a search bar and a list of search results
 *         The search bar is used to search for complex algorithms
 *         The search results are displayed in a list
 *         You can navigate to the particular algorithm by clicking on the search result
 *         and the new navigation screen which is @ComplexSearchAlgoScreen
 * */
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.ComplexRoutes
import com.prafull.algorithms.R
import com.prafull.algorithms.commons.CustomSearchBar
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.screens.complexSearch.main.ComplexSearchVM
import com.valentinilk.shimmer.shimmer

@Composable
fun ComplexSearchScreen(complexVm: ComplexSearchVM, navController: NavController) {
    var searchQuery by rememberSaveable {
        mutableStateOf(complexVm.searchQuery)
    }
    LaunchedEffect(key1 = searchQuery) {
        complexVm.searchQuery = searchQuery
    }
    val state by complexVm.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        item {
            CustomSearchBar(
                leadingIcon = Icons.Default.ArrowBack,
                leadingIconEnabled = true, value = searchQuery, onValueChange = {
                    searchQuery = it
                }, modifier = Modifier.padding(16.dp),
                onLeadingIconClick = navController::goBackStack,
                onSearch = {
                    complexVm.search(searchQuery)
                    focusManager.clearFocus()
                }
            )
        }
        if (state.loading) {
            items(15) {
                ShimmerSearch(Modifier.fillMaxWidth())
            }
        } else if (state.error.first) {
            item {
                Text(text = state.error.second.message ?: "An error occurred")
            }
        } else {
            itemsIndexed(state.searchResults, key = { idx, _ ->
                idx
            }) { _, result ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                ComplexRoutes.ComplexSearchResultScreen(
                                    result
                                )
                            )
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = result.replace("+", " ").replace("-", " ").capitalize(),
                        modifier = Modifier.weight(.85f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_outward_24),
                        contentDescription = null,
                        modifier = Modifier.weight(.15f)
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerSearch(modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .shimmer()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.Gray)
        )
    }
}