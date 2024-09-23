package com.prafull.algorithms.complexSearch.ui.searchedAlgo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.commons.components.CustomSearchBar
import com.prafull.algorithms.commons.ui.customColors.langColor
import com.prafull.algorithms.complexSearch.ComplexRoutes
import com.prafull.algorithms.complexSearch.domain.models.ComplexAlgorithm
import com.prafull.algorithms.complexSearch.ui.main.getIcon


@Composable
fun AlgoSuccessScreen(
    algo: ComplexAlgorithm, navController: NavController
) {
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    val textState = rememberRichTextState()
    LaunchedEffect(key1 = Unit) {
        textState.setMarkdown(algo.task)
    }

    val focusManager = LocalFocusManager.current
    val filteredLanguages by remember(algo.languages, searchQuery) {
        derivedStateOf {
            algo.languages.filter { language ->
                language.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    val navigateAlgoImpl: (String) -> Unit = remember {
        { langName ->
            navController.navigate(
                ComplexRoutes.ComplexLanguageAlgoRoute(
                    algo = algo.id, lang = langName
                )
            )
        }
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                RichText(state = textState, modifier = Modifier.padding(8.dp))
            }
        }
        item {
            CustomSearchBar(value = searchQuery, onValueChange = {
                searchQuery = it
            }, showSearchButton = false, label = "Search Languages", onSearch = {
                focusManager.clearFocus()
            })
        }

        items(filteredLanguages, key = {
            it
        }) {
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.langColor()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateAlgoImpl(it)
                        }
                        .padding(12.dp),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(.8f),
                        verticalAlignment = CenterVertically,
                    ) {
                        Text(
                            text = it
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = getIcon(it)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = {
                        navigateAlgoImpl(it)
                    }, Modifier.weight(.2f)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Send,
                            contentDescription = "Getting to particular algo screen"
                        )
                    }
                }
            }
        }
    }
}