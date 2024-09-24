package com.prafull.algorithms.complexSearch.ui.langAlgoScreen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.ads.BannerAdView
import com.prafull.algorithms.commons.ads.InterstitialAdManager
import com.prafull.algorithms.commons.components.AskAiChip
import com.prafull.algorithms.commons.components.AskAiDialog
import com.prafull.algorithms.commons.components.ErrorComposable
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.commons.utils.getFormattedNameExtension
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageAlgo
import com.prafull.algorithms.goBackStack
import com.valentinilk.shimmer.shimmer
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageAlgoScreen(viewModel: ComplexLanguageAlgoVM, navController: NavController) {
    val state by viewModel.problemDetails.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showWarning by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = viewModel.selectedAlgo.getFormattedNameExtension())
                },
                navigationIcon = {
                    IconButton(onClick = navController::goBackStack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showWarning = showWarning.not()
                    }) {
                        Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BannerAdView(adUnitId = Const.COMPLEX_SCREEN_LANGUAGE_BANNER)
            when (state) {
                is BaseClass.Error -> {
                    ErrorComposable(exception = (state as BaseClass.Error).exception) {
                        viewModel.getProblemDetails()
                    }
                }

                BaseClass.Loading -> {
                    LanguageAlgoLoading()
                }

                is BaseClass.Success -> {
                    val algo = (state as BaseClass.Success<ComplexLanguageAlgo>).data
                    LanguageAlgoSuccess(
                        algo = DummyAlgoData(
                            task = algo.task,
                            codes = algo.langCode,
                            shorterTask = if (algo.task.length > 500) algo.task.substring(
                                0, 500
                            ) + "..." else algo.task,
                            language = viewModel.lang,
                            programName = viewModel.selectedAlgo.getFormattedNameExtension()
                        ), navController = navController, viewModel
                    )
                }
            }
        }
        if (showWarning) {
            InfoAlertDialog {
                showWarning = !showWarning
            }
        }
    }
}

@Composable
fun InfoAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        Button(onClick = {
            onDismiss()
        }) {
            Text("OK")
        }
    }, title = {
        Text(text = "Information")
    }, text = {
        Text("The code provided may not be accurate. You can ask AI for better results.")
    }, properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Immutable
data class DummyAlgoData(
    val task: String,
    val codes: List<String>,
    val shorterTask: String = "",
    val language: String,
    val programName: String = ""
)

@Composable
private fun LanguageAlgoSuccess(
    algo: DummyAlgoData, navController: NavController, viewModel: ComplexLanguageAlgoVM
) {
    val taskState = rememberRichTextState()
    LaunchedEffect(key1 = Unit) {
        taskState.setMarkdown(algo.shorterTask)
    }
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val uiMode = LocalConfiguration.current.uiMode
    var isDark by remember {
        mutableStateOf((uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
    }
    LaunchedEffect(key1 = uiMode) {
        isDark = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "task") {
            ElevatedCard(onClick = {
                isOpen = !isOpen
                taskState.setMarkdown(if (isOpen) algo.task else algo.shorterTask)
            }, enabled = algo.task.length > 500) {
                Text(
                    text = "Problem Statement",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp)
                )
                RichText(
                    state = taskState, modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = if (isOpen) "Read Less" else "Read More",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )

                }
            }
        }
        itemsIndexed(algo.codes, key = { idx, _ ->
            idx
        }) { idx, code ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.dp, color = Color.LightGray
                        ), shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "Code ${idx + 1}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
                val highlights = remember {
                    Highlights.Builder(code = code)
                        .language(getSyntaxLanguageFromString(algo.language)).theme(
                            SyntaxThemes.darcula()
                        ).build()
                }
                CodeTextView(highlights = highlights, modifier = Modifier.padding(12.dp))
                NavigateToAiChip(
                    algo = algo, navController = navController, idx = idx, viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun NavigateToAiChip(
    modifier: Modifier = Modifier,
    algo: DummyAlgoData,
    navController: NavController,
    idx: Int,
    viewModel: ComplexLanguageAlgoVM
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showInterstitialAd by rememberSaveable {
        mutableStateOf(false)
    }
    var currMessage by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp), horizontalArrangement = Arrangement.End
    ) {
        AskAiChip {
            if (viewModel.isKeySaved(context)) {
                showDialog = true
            } else {
                Toast.makeText(context, "Please save your API Key first", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.EnrollToAiRoute)
            }
        }
    }
    if (showDialog) {
        AskAiDialog(onDismiss = {
            showDialog = !showDialog
        }, onSend = {
            showDialog = !showDialog
            currMessage = it
            showInterstitialAd = true
        })
    }
    if (showInterstitialAd) {
        InterstitialAdManager(adUnitId = "ca-app-pub-3940256099942544/1033173712") {
            showInterstitialAd = false
            navController.navigate(
                Routes.AskAi(
                    code = algo.codes[idx],
                    language = algo.language,
                    message = currMessage,
                    programName = algo.programName
                )
            )
        }
    }
}

@Composable
private fun LanguageAlgoLoading() {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray)
                    .shimmer()
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray)
                    .shimmer()
            )
        }
    }
}

fun getSyntaxLanguageFromString(langName: String): SyntaxLanguage {
    return when (langName) {
        "C" -> SyntaxLanguage.C
        "C++" -> SyntaxLanguage.CPP
        "C-sharp" -> SyntaxLanguage.CSHARP
        "Java" -> SyntaxLanguage.JAVA
        "JavaScript" -> SyntaxLanguage.JAVASCRIPT
        "Kotlin" -> SyntaxLanguage.KOTLIN
        "Rust" -> SyntaxLanguage.RUST
        "CoffeeScript" -> SyntaxLanguage.COFFEESCRIPT
        "Perl" -> SyntaxLanguage.PERL
        "Perl5i" -> SyntaxLanguage.PERL
        "Python" -> SyntaxLanguage.PYTHON
        "Ruby" -> SyntaxLanguage.RUBY
        "Friendly-interactive-shell" -> SyntaxLanguage.SHELL
        "PowerShell" -> SyntaxLanguage.SHELL
        "SheerPower-4GL" -> SyntaxLanguage.SHELL
        "UNIX-Shell" -> SyntaxLanguage.SHELL
        "Swift" -> SyntaxLanguage.SWIFT
        else -> SyntaxLanguage.MIXED
    }
}