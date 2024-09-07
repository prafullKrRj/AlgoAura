package com.prafull.algorithms.screens.complexSearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.CustomSearchBar

@Composable
fun ComplexSearchScreen(complexVm: ComplexSearchVM, navController: NavHostController) {
    val state by complexVm.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    LazyColumn(
        Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp), verticalArrangement =
        Arrangement.spacedBy(8.dp)
    ) {
        item {
            CustomSearchBar(value = complexVm.searchQuery, onValueChange = {
                complexVm.searchQuery = it
            }) {
                complexVm.search()
                focusManager.clearFocus()
            }
        }
        if (state.loading) {
            items(15) {
                ShimmerCard(Modifier.fillMaxWidth())
            }
        } else if (state.error.first) {
            item {
                Text(text = state.error.second.message ?: "An error occurred")
            }
        } else {
            items(state.searchResults) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clickable {
                            navController.navigate(Routes.ComplexSearchResultScreen(it))
                        }, shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = it.replace("+", " ").replace("-", " ").capitalize(),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}