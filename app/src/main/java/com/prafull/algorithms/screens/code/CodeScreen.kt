package com.prafull.algorithms.screens.code

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.AskAiChip
import com.prafull.algorithms.commons.CodeScreenBottomBar
import com.prafull.algorithms.commons.CodeScreenTopAppBar
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.getKodeViewLanguageFromLanguage
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView

@Composable
fun CodeScreen(viewModel: CodeViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val onFavClick = remember {
        {
            viewModel.addToDb()
        }
    }
    val selected = false        // TODO: Implement this

    Scaffold(
        topBar = {
            CodeScreenTopAppBar(
                isFavorite = selected,
                onFavClick = onFavClick,
                onBackClick = navController::goBackStack,
                title = viewModel.programName,
                showToggle = viewModel.algorithm != null
            )
        },
        bottomBar = {
            if (viewModel.algorithm != null) {
                CodeScreenBottomBar(viewModel.algorithm!!.code)
            }
        },
        floatingActionButton = {
            if (viewModel.algorithm != null) {
                AskAiChip {
                    navController.navigate(Routes.AskAi)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {

                is BaseClass.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is BaseClass.Success -> {
                    val successState = state as BaseClass.Success
                    val code = (state as BaseClass.Success).data.code

                    val highlights = remember {
                        Highlights
                            .Builder(code = code)
                            .theme(SyntaxThemes.darcula())
                            .language(language = getKodeViewLanguageFromLanguage(successState.data.language))
                            .build()
                    }
                    CodeTextView(highlights = highlights)
                }

                is BaseClass.Error -> {
                    //Text(text = (state as BaseClass.Error).message)
                }
            }
        }
    }
}