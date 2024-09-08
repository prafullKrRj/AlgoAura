package com.prafull.algorithms.screens.complexSearch.lang.algoScreen

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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.models.ComplexLanguageAlgo
import com.prafull.algorithms.utils.BaseClass
import com.valentinilk.shimmer.shimmer
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageAlgoScreen(viewModel: ComplexLanguageAlgoVM, navController: NavController) {
    val state by viewModel.problemDetails.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = viewModel.selectedAlgo)
        }, navigationIcon = {
            IconButton(onClick = navController::goBackStack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is BaseClass.Error -> {
                    Text(text = (state as BaseClass.Error).message)
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
                            ) + "..." else algo.task
                        )
                    )
                }
            }
        }
    }
}

data class DummyAlgoData(
    val task: String, val codes: List<String>, val shorterTask: String = ""
)

@Composable
private fun LanguageAlgoSuccess(algo: DummyAlgoData) {
    val taskState = rememberRichTextState()
    LaunchedEffect(key1 = Unit) {
        taskState.setMarkdown(algo.shorterTask)
    }
    var isOpen by rememberSaveable {
        mutableStateOf(false)
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
                            width = 2.dp, color = Color.LightGray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "Code ${idx + 1}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
                val highlights = remember {
                    Highlights.Builder(code = code).theme(SyntaxThemes.darcula())
                        .build()
                }
                CodeTextView(highlights = highlights, modifier = Modifier.padding(8.dp))
            }

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