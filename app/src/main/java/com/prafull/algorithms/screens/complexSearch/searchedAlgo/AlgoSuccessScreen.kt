package com.prafull.algorithms.screens.complexSearch.searchedAlgo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.commons.CustomSearchBar
import com.prafull.algorithms.models.ComplexAlgorithm


@Composable
fun AlgoSuccessScreen(algo: ComplexAlgorithm, paddingValues: PaddingValues) {
    val textState = rememberRichTextState()
    textState.setMarkdown(algo.task)
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                RichText(state = textState, modifier = Modifier.padding(8.dp))
            }
        }
        item {
            CustomSearchBar(value = "", onValueChange = {}) {

            }
        }
        items(algo.languages) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(text = it, modifier = Modifier.padding(8.dp))
            }
        }
    }
}