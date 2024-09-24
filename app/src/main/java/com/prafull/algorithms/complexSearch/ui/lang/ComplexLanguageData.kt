package com.prafull.algorithms.complexSearch.ui.lang

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import com.prafull.algorithms.commons.components.ErrorComposable
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageData
import com.prafull.algorithms.goBackStack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexLanguageData(viewModel: ComplexLanguageViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(text = viewModel.langName)
            }, navigationIcon = {
                IconButton(onClick = navController::goBackStack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        when (state) {
            is BaseClass.Error -> {
                ErrorComposable(
                    exception = (state as BaseClass.Error).exception,
                    onRetry = viewModel::getLangData
                )
            }

            BaseClass.Loading -> {
                Text(text = "Loading")
            }

            is BaseClass.Success -> {
                ComplexLanguageSuccess(
                    data = (state as BaseClass.Success<ComplexLanguageData>).data,
                    paddingValues,
                    viewModel,
                    navController
                )
            }
        }
    }
}
