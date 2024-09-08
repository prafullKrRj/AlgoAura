package com.prafull.algorithms.screens.complexSearch.searchedAlgo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.models.ComplexAlgorithm
import com.prafull.algorithms.utils.BaseClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexSearchResultScreen(viewModel: ComplexSearchAlgoVM, navController: NavController) {
    val state by viewModel.algoDetails.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = viewModel.algo)
        }, navigationIcon = {
            IconButton(onClick = navController::goBackStack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        when (state) {
            is BaseClass.Loading -> {
                AlgoShimmerScreen(paddingValues)
            }

            is BaseClass.Success -> {
                AlgoSuccessScreen(
                    algo = (state as BaseClass.Success<ComplexAlgorithm>).data,
                    paddingValues = paddingValues
                )
            }

            is BaseClass.Error -> {
                Text(text = "Error")
            }
        }
    }
}

@Composable
fun AlgoShimmerScreen(paddingValues: PaddingValues) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(15) {
            Card(modifier = Modifier.fillMaxWidth()) {
                RichText(state = rememberRichTextState().apply {
                    setHtml("<h1>Shimmer</h1>")
                })
            }
        }
    }
}
