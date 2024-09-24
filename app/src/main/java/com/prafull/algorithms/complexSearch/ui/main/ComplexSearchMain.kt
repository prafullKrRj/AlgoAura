package com.prafull.algorithms.complexSearch.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.commons.components.CustomSearchBar
import com.prafull.algorithms.commons.components.ErrorComposable
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.complexSearch.ComplexRoutes
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexSearchMain(viewModel: ComplexSearchVM, navController: NavController) {
    val complexLanguagesState by viewModel.langs.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar(
            title = {
                Text(text = "Detailed Search")
            }, scrollBehavior = scrollBehavior
        )
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            CustomSearchBar(value = viewModel.searchQuery, onValueChange = {
                viewModel.searchQuery = it
            }) {
                viewModel.search(viewModel.searchQuery)
                focusManager.clearFocus()
                navController.navigate(ComplexRoutes.ComplexSearchScreen)
            }

            when (complexLanguagesState) {
                is BaseClass.Error -> {
                    ErrorComposable(exception = (complexLanguagesState as BaseClass.Error).exception) {
                        viewModel.getComplexLanguagesList()
                    }
                }

                BaseClass.Loading -> {
                    LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
                        items(15) {
                            ShimmerCardMainScreen(Modifier.padding(6.dp))
                        }
                    }
                }

                is BaseClass.Success -> {
                    LanguageSuccess(
                        navController = navController,
                        languages = (complexLanguagesState as BaseClass.Success<List<String>>).data
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerCardMainScreen(modifier: Modifier = Modifier) {
    Card(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .shimmer(), shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.Gray)
        )
    }
}