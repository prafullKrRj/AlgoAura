package com.prafull.algorithms.screens.code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.ads.BannerAdView
import com.prafull.algorithms.commons.ads.InterstitialAdManager
import com.prafull.algorithms.commons.components.AskAiDialog
import com.prafull.algorithms.commons.components.CodeScreenBottomBar
import com.prafull.algorithms.commons.components.CodeScreenTopAppBar
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.screens.ai.PromptField
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.getFileName
import com.prafull.algorithms.utils.getFormattedName
import com.prafull.algorithms.utils.getKodeViewLanguageFromLanguage
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView

@Composable
fun CodeScreen(viewModel: CodeViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val onFavClick = remember {
        {
            viewModel.toggleFav()
        }
    }
    // TODO: Implement this
    var goToAiDialogBox by remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        CodeScreenTopAppBar(
            isFavorite = viewModel.isFav,
            onFavClick = onFavClick,
            onBackClick = navController::goBackStack,
            title = getFormattedName(getFileName(viewModel.programName)),
            showToggle = viewModel.algorithm != null
        )
    }, bottomBar = {
        if (viewModel.algorithm != null) {
            CodeScreenBottomBar(viewModel.algorithm!!.code)
        }
    }, floatingActionButton = {
        /*   if (viewModel.algorithm != null) {
               AskAiChip {
                   goToAiDialogBox = true
               }
           }*/
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            BannerAdView()
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
                        Highlights.Builder(code = code).theme(SyntaxThemes.darcula())
                            .language(language = getKodeViewLanguageFromLanguage(successState.data.language))
                            .build()
                    }
                    CodeTextView(highlights = highlights)
                }


                is BaseClass.Error -> {
                    Text(text = (state as BaseClass.Error).message)
                }
            }
        }
    }
    var showInterstitialAd by rememberSaveable {
        mutableStateOf(false)
    }
    var currMessage by rememberSaveable {
        mutableStateOf("")
    }
    if (goToAiDialogBox) {
        AskAiDialog(onDismiss = {
            goToAiDialogBox = false
        }) {
            goToAiDialogBox = false
            showInterstitialAd = true
            currMessage = it
        }
    }
    if (showInterstitialAd) {
        InterstitialAdManager(adUnitId = "ca-app-pub-3940256099942544/1033173712") {
            showInterstitialAd = false
            navController.navigate(
                Routes.AskAi(
                    code = viewModel.algorithm!!.code,
                    programName = viewModel.programName,
                    message = currMessage,
                    language = viewModel.algorithm!!.language.name
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoToAiDialog(
    onDismiss: () -> Unit = {}, navController: NavController, askAi: Routes.AskAi
) {
    val promptValue = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val predefinedPrompts = listOf("Help with syntax", "Optimize code", "Explain this code")
    val selectedPrompt = remember {
        mutableIntStateOf(-1)
    }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Select a prompt or enter your own:",
                    style = MaterialTheme.typography.headlineSmall
                )

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    predefinedPrompts.forEachIndexed { index, prompt ->
                        FilterChip(label = {
                            Text(text = prompt)
                        }, selected = index == selectedPrompt.intValue, onClick = {
                            selectedPrompt.intValue = index
                            promptValue.value = prompt
                        })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                PromptField(promptValue.value, {
                    promptValue.value = it
                }) {
                    if (promptValue.value.isNotEmpty()) {
                        navController.navigate(
                            askAi.copy(
                                message = promptValue.value
                            )
                        )
                    }
                    promptValue.value = ""
                    focusManager.clearFocus()
                }

                Spacer(modifier = Modifier.height(8.dp))

                FilledTonalButton(onClick = {
                    if (promptValue.value.isNotEmpty()) {
                        navController.navigate(
                            askAi.copy(
                                message = promptValue.value
                            )
                        )
                        promptValue.value = ""
                        focusManager.clearFocus()
                    }
                }) {
                    Text("Submit")
                }
            }
        }
    }
}