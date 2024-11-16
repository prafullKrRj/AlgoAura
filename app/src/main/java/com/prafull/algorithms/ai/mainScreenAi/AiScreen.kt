package com.prafull.algorithms.ai.mainScreenAi

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.prafull.algorithms.ai.ChatMessageBubble
import com.prafull.algorithms.ai.PromptField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiScreen(viewModel: AiViewModel) {
    val promptValue = rememberSaveable {
        mutableStateOf("")
    }
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("AlgoAura Bot")
            })
        },
        bottomBar = {
            PromptField(value = promptValue.value, onValueChange = {
                promptValue.value = it
            }) {
                viewModel.sendMessage(promptValue.value)
                promptValue.value = ""
                focusManager.clearFocus()
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 8.dp,
                end = 8.dp
            ),
            modifier = Modifier
                .fillMaxSize(),
            reverseLayout = true,
        ) {
            items(state.messages.reversed(), key = {
                it.id
            }) {
                ChatMessageBubble(it, clipboardManager, context)
            }
        }
    }
}