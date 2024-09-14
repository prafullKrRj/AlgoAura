package com.prafull.algorithms.screens.complexSearch.lang

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.ComplexRoutes
import com.prafull.algorithms.commons.components.CustomSearchBar
import com.prafull.algorithms.models.ComplexLanguageData
import com.prafull.algorithms.ui.customColors.algoCard
import com.prafull.algorithms.utils.getFormattedNameExtension


@Composable
fun ComplexLanguageSuccess(
    data: ComplexLanguageData,
    paddingValues: PaddingValues,
    viewModel: ComplexLanguageViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    var value by rememberSaveable {
        mutableStateOf("")
    }
    val searchedResults by viewModel.searchedProblems.collectAsState()
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(paddingValues), contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item(key = "desc") {
            LanguageDescription(desc = data.langDescription)
        }
        if (searchedResults.isEmpty()) {
            item {
                Text(
                    text = "No Problems Found",
                    modifier = Modifier.padding(8.dp),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        } else {
            item("Search Problem") {
                CustomSearchBar(label = "Search Problems", value = value, onValueChange = {
                    value = it
                }) {
                    viewModel.filterProblems(value)
                    focusManager.clearFocus()
                }
            }
            items(searchedResults, key = {
                it.name
            }) {
                ProblemCard(
                    problemName = it.name,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun ProblemCard(
    problemName: String,
    viewModel: ComplexLanguageViewModel,
    navController: NavController
) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.algoCard()) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(
                        ComplexRoutes.ComplexLanguageAlgoRoute(
                            problemName, viewModel.langName
                        )
                    )
                }
                .padding(8.dp)
        ) {
            Text(text = problemName.getFormattedNameExtension())
        }
    }
}


@Composable
private fun LanguageDescription(desc: String) {
    if (desc.isEmpty()) return
    val textState = rememberRichTextState()
    var openingRequired by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        if (desc.length >= 300) {
            openingRequired = true
            textState.setMarkdown(desc.substring(0, 300) + "...")
        } else {
            textState.setMarkdown(desc)
        }
    }
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = openingRequired
                ) {
                    isOpen = isOpen.not()
                    if (isOpen) textState.setMarkdown(desc)
                    else textState.setMarkdown(desc.substring(0, 300) + "...")
                }
                .padding(8.dp)
        ) {
            Text(text = "Description", style = MaterialTheme.typography.headlineSmall)
            RichText(state = textState)
            if (openingRequired) {
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        isOpen = isOpen.not()
                        if (isOpen) textState.setMarkdown(desc)
                        else textState.setMarkdown(desc.substring(0, 300) + "...")
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(
                                if (isOpen) 180f else 0f
                            )
                        )
                    }
                }
            }
        }
    }
}