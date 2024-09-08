package com.prafull.algorithms.screens.complexSearch.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.ComplexRoutes
import com.prafull.algorithms.commons.CustomSearchBar
import com.prafull.algorithms.utils.BaseClass
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexSearchMain(viewModel: ComplexSearchVM, navController: NavController) {
    val complexLanguagesState by viewModel.langs.collectAsState()
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Detailed Search")
            })
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            item {
                CustomSearchBar(value = viewModel.searchQuery, onValueChange = {
                    viewModel.searchQuery = it
                }) {
                    viewModel.search()
                    focusManager.clearFocus()
                    navController.navigate(ComplexRoutes.ComplexSearchScreen)
                }
            }
            when (complexLanguagesState) {
                is BaseClass.Error -> {
                    item {
                        Text(text = (complexLanguagesState as BaseClass.Error).message)
                    }
                }

                BaseClass.Loading -> {
                    items(10) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ShimmerCard(Modifier.weight(.5f))
                            ShimmerCard(Modifier.weight(.5f))
                        }
                    }
                }

                is BaseClass.Success -> {
                    item {
                        Text(
                            text = "Languages",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    items(
                        (complexLanguagesState as BaseClass.Success<List<String>>).data.chunked(2),
                        key = { it.joinToString() }
                    ) {
                        LanguageSuccess(navController = navController, languages = it)
                    }
                }
            }
        }
    }
}


@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
    Card(
        modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .shimmer(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.Gray)
        )
    }
}