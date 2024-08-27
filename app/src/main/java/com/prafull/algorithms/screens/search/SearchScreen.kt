package com.prafull.algorithms.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.R
import com.prafull.algorithms.data.local.SearchedEntity
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.getFileName
import com.prafull.algorithms.utils.getFormattedName
import com.prafull.algorithms.utils.getLanguageFromString
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val searchedElements by viewModel.searchedElements.collectAsState()
    val languagesListState = rememberLazyListState()
    LaunchedEffect(key1 = Unit) {
        if (viewModel.searchResults.isEmpty()) {
            focusRequester.requestFocus()
        }
    }
    val selectedLang = rememberSaveable {
        mutableIntStateOf(-1)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "🔍 Search")
            })
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .focusRequester(focusRequester)
        ) {
            OutlinedTextField(value = viewModel.query, onValueChange = {
                viewModel.query = it
            }, label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.search(
                            SearchedEntity(searchedText = viewModel.query)
                        )
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.search(
                            SearchedEntity(searchedText = viewModel.query)
                        )
                        focusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Search Icon")
                    }
                }
            )
            if (viewModel.loading) {
                LoadingShimmerSearchScreen()
            } else if (viewModel.error.isNotEmpty()) {
                Text(text = viewModel.error)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { _, _ -> focusManager.clearFocus() }
                        },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    if (viewModel.searchResults.isNotEmpty()) {
                        item {
                            LazyRow(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                state = languagesListState
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedLang.intValue == -1,
                                        onClick = {
                                            selectedLang.intValue = -1
                                            viewModel.filterResults(ProgrammingLanguage.UNKNOWN)
                                        },
                                        label = {
                                            Text(text = "All")
                                        },
                                        trailingIcon = {
                                            Image(
                                                painter = painterResource(id = R.drawable.baseline_code_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                colorFilter = ColorFilter.tint(color = Color.Cyan)
                                            )
                                        })
                                }
                                itemsIndexed(viewModel.languages) { index, programmingLanguage ->
                                    FilterChip(
                                        selected = index == selectedLang.intValue,
                                        onClick = {
                                            selectedLang.intValue = index
                                            viewModel.filterResults(programmingLanguage)
                                        },
                                        label = {
                                            Text(text = programmingLanguage.languageGenerics)
                                        },
                                        trailingIcon = {
                                            Image(
                                                painter = painterResource(id = programmingLanguage.logo),
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        })
                                }
                            }
                        }
                        items(viewModel.results, key = {
                            it.id
                        }) { fileInfo ->
                            SearchItem(fileInfo = fileInfo, navController = navController)
                        }
                    } else {
                        items(searchedElements) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.query = it.searchedText
                                        viewModel.search(it)
                                        focusManager.clearFocus()
                                    }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(text = it.searchedText)
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_arrow_outward_24),
                                    contentDescription = "To Search"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingShimmerSearchScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    ) {

        items(15) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .weight(.85f)
                    ) {
                        Text(
                            text = "",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .alpha(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .weight(.15f)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchItem(fileInfo: FileInfo, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(
                    fileInfo.toCodeScreen()
                )
            }
            .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(.85f)
            ) {
                Text(
                    text = getFormattedName(getFileName(fileInfo.name)).capitalize(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Image(
                painter = painterResource(id = getLanguageFromString(fileInfo.path).logo),
                contentDescription = null,
                modifier = Modifier
                    .weight(.15f)
                    .alpha(1f)
            )
        }
    }
}