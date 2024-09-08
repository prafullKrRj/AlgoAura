package com.prafull.algorithms.screens.complexSearch.lang

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.utils.BaseClass


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageAlgoScreen(viewModel: ComplexLanguageAlgoVM, navController: NavController) {
    val state by viewModel.problemDetails.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = viewModel.selectedAlgo)
            }, navigationIcon = {
                IconButton(onClick = navController::goBackStack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { paddingValues ->
        when (state) {
            is BaseClass.Error -> {
                Text(text = "Error")
            }

            BaseClass.Loading -> {
                Text(text = "Loading")
            }

            is BaseClass.Success -> {
                val data = (state as BaseClass.Success).data
                //Text(text = data.task, modifier = Modifier.padding(paddingValues))
                data.langCode.forEach {
                    Text(text = it)
                }
            }
        }
    }
}