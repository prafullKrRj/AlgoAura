package com.prafull.algorithms.screens.ai

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.screens.AiMessageBubble
import com.prafull.algorithms.screens.complexSearch.algoScreen.getSyntaxLanguageFromString
import com.prafull.algorithms.utils.getFileName
import com.prafull.algorithms.utils.getFormattedName
import com.prafull.algorithms.utils.getKodeViewLanguageFromLanguage
import com.prafull.algorithms.utils.getLanguageFromString
import com.valentinilk.shimmer.shimmer
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskAi(viewModel: ChatViewModel, navController: NavController) {
    val codeExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    var language = SyntaxLanguage.DEFAULT
    LaunchedEffect(key1 = Unit) {
        language = if (getLanguageFromString(viewModel.language) == ProgrammingLanguage.UNKNOWN) {
            getSyntaxLanguageFromString(viewModel.language)
        } else {
            getKodeViewLanguageFromLanguage(ProgrammingLanguage.valueOf(viewModel.language))
        }
    }
    val state by viewModel.uiState.collectAsState()
    val promptValue = rememberSaveable {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Scroll to the bottom when a new message is added
    LaunchedEffect(state.messages.size) {
        listState.animateScrollToItem(0)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = getFileName(getFormattedName(viewModel.programName)))
        }, actions = {
            IconButton(onClick = {
                codeExpanded.value = !codeExpanded.value
                if (codeExpanded.value) {
                    scope.launch {
                        listState.animateScrollToItem(state.messages.size) // as the code is at the end
                    }
                } else {
                    scope.launch {
                        listState.animateScrollToItem(0) // as the code is at the end
                    }
                }
            }) {
                Icon(
                    imageVector = if (codeExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }, navigationIcon = {
            IconButton(onClick = navController::goBackStack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null
                )
            }
        })
    }, bottomBar = {
        PromptField(promptValue.value, {
            promptValue.value = it
        }) {
            viewModel.sendMessage(promptValue.value)
            promptValue.value = ""
            focusManager.clearFocus()
        }
    }) { paddingValues ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
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
            item {
                AnimatedCode(
                    visible = codeExpanded.value,
                    code = viewModel.code,
                    language = language
                )
            }
        }
    }
}

@Composable
fun PromptField(value: String, onValueChange: (String) -> Unit, onSent: () -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange,
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp), label = {
            Text(text = "Ask me anything")
        }, trailingIcon = {
            IconButton(onClick = onSent) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null
                )
            }
        }, colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ), keyboardActions = KeyboardActions(onSend = {
            onSent()
        })
    )
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    clipboardManager: ClipboardManager,
    context: Context
) {
    if (message.participant == Participant.USER) {
        if (message.isPending) {
            Row(
                modifier = Modifier
                    .shimmer() // <- Affects all subsequent UI elements
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(text = "✨", Modifier.padding(vertical = 12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(modifier = Modifier) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .weight(.8f)
                                .height(24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                        )
                        Spacer(modifier = Modifier.weight(.2f))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .weight(.65f)
                                .height(24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                        )
                        Spacer(modifier = Modifier.weight(.35f))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .weight(.5f)
                                .height(24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                        )
                        Spacer(modifier = Modifier.weight(.5f))
                    }
                }
            }
        }
        UserChatBubble(message)

    } else {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "✨", Modifier.padding(vertical = 12.dp))
            ModelChatBubble(message, clipboardManager, context)
        }
    }
}

@Composable
fun UserChatBubble(message: ChatMessage) {
    Row(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(.3f))
        Card(
            modifier = Modifier.weight(.7f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            val textState = rememberRichTextState()
            textState.setMarkdown(message.text)
            RichText(
                state = textState,
                Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun ModelChatBubble(
    message: ChatMessage,
    clipboardManager: ClipboardManager,
    context: Context
) {
    Row(Modifier.fillMaxWidth()) {
        AiMessageBubble(message = message, clipboardManager, context)
    }
}

@Composable
private fun AnimatedCode(visible: Boolean, code: String, language: SyntaxLanguage) {
    val context = LocalContext.current
    val highlights = remember {
        Highlights.Builder(code = code).theme(SyntaxThemes.darcula()).language(
            language = language
        ).build()
    }
    AnimatedVisibility(
        visible = visible, enter = fadeIn(
            animationSpec = tween(
                durationMillis = 300, easing = FastOutLinearInEasing
            )
        ), exit = fadeOut(), modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            CodeTextView(highlights = highlights)
        }
    }
}