package com.prafull.algorithms.dsaSheet.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prafull.algorithms.R
import com.prafull.algorithms.commons.ui.customColors.normalSearchBarColors
import com.prafull.algorithms.dsaSheet.DsaSheetRoutes
import com.prafull.algorithms.dsaSheet.data.local.QuestionEntity
import com.prafull.algorithms.dsaSheet.data.local.TopicEntity
import com.prafull.algorithms.enrollToAi.howToCreateApiKey.goToWebsite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DsaSheetScreen(viewModel: DsaSheetViewModel, navController: NavController) {
    val topics by viewModel.topicWithQuestions.collectAsState()
    val context = LocalContext.current
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var utilExpanded by rememberSaveable { mutableStateOf(false) }
    val utilRotationState by animateFloatAsState(
        targetValue = if (utilExpanded) 180f else 0f, label = ""
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            shape = RoundedCornerShape(24.dp),
                            placeholder = { Text("Search questions") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                            }),
                            colors = OutlinedTextFieldDefaults.normalSearchBarColors(),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                        LaunchedEffect(focusRequester) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Text(text = "📃 DSA Sheet", style = MaterialTheme.typography.headlineSmall)
                    }
                },
                actions = {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (!isSearchActive) {
                            IconButton(onClick = { utilExpanded = !utilExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Expand",
                                    modifier = Modifier
                                        .rotate(utilRotationState),
                                )
                            }
                        }
                        IconButton(onClick = {
                            if (isSearchActive) {
                                isSearchActive = false
                                searchQuery = ""
                                keyboardController?.hide()
                            } else {
                                isSearchActive = true
                            }
                        }) {
                            Icon(
                                imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = if (isSearchActive) "Close search" else "Open search"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (utilExpanded) {
            ModalBottomSheet(onDismissRequest = {
                utilExpanded = false
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DsaBottomSheetItems("Go to Solved") {
                        utilExpanded = false
                        navController.navigate(DsaSheetRoutes.DsaSolvedQuestionScreen)
                    }
                    DsaBottomSheetItems("Go to Revision") {
                        utilExpanded = false
                        navController.navigate(DsaSheetRoutes.DsaRevisionScreen)
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(topics.filter { topic ->
                topic.questions.any { question ->
                    question.name.contains(searchQuery, ignoreCase = true)
                }
            }, key = { idx, _ -> idx }) { idx, topicWithQuestions ->
                TopicCard(
                    idx,
                    topic = topicWithQuestions.topic,
                    questions = topicWithQuestions.questions.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    },
                    context,
                    viewModel,
                    navController
                )
            }
        }
    }
}

@Composable
fun DsaBottomSheetItems(text: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text, modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(start = 8.dp)
        )
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                contentDescription = "Expand"
            )
        }
    }
}

@Composable
fun TopicCard(
    idx: Int,
    topic: TopicEntity,
    questions: List<QuestionEntity>,
    context: Context,
    viewModel: DsaSheetViewModel,
    navController: NavController
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = ""
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = topic.topicName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(.85f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painter = painterResource(id = if (expanded) R.drawable.baseline_expand_less_24 else R.drawable.baseline_expand_more_24),
                        contentDescription = "Expand",
                        modifier = Modifier
                            .weight(.15f),
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = topic.topicDetails,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Header Row for descriptions
                    HeaderRow()

                    Spacer(modifier = Modifier.height(8.dp))

                    questions.forEach { question ->
                        QuestionRow(
                            topic.topicName,
                            question,
                            context,
                            viewModel,
                            navController
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        DsaSheetScreenData.tooltipData.forEach {
            Box(modifier = Modifier.weight(it.weight)) {
                RichTooltip(data = it, modifier = Modifier)
            }
        }
    }
}

@Composable
fun QuestionRow(
    topic: String,
    question: QuestionEntity,
    context: Context,
    viewModel: DsaSheetViewModel,
    navController: NavController
) {
    var note by remember { mutableStateOf("") }
    var showNoteDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    DsaSheetRoutes.DsaQuestionScreen(
                        topic = topic,
                        question = question.name,
                        link = question.link
                    )
                )
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = question.solved, onCheckedChange = { isChecked ->
                viewModel.onSolvedChanged(question, isChecked)
            }, modifier = Modifier.weight(0.15f), colors = CheckboxDefaults.colors()
        )
        Text(
            text = question.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.50f)
        )

        Checkbox(
            checked = question.revision, onCheckedChange = { isChecked ->
                viewModel.onRevisionChanged(question, isChecked)
            }, modifier = Modifier.weight(0.15f), colors = CheckboxDefaults.colors()
        )

        IconButton(
            onClick = {
                showNoteDialog = true
                note = question.note
            }, modifier = Modifier.weight(0.10f)
        ) {
            Icon(
                painter = painterResource(id = if (question.note.isEmpty()) R.drawable.baseline_note_add_24 else R.drawable.baseline_note_24),
                contentDescription = "Note"
            )
        }

        AsyncImage(model = ImageRequest.Builder(context).data(R.drawable.leetcode).build(),
            contentDescription = "leetcode_image",
            alpha = 1f,
            modifier = Modifier
                .weight(.10f)
                .size(24.dp)
                .clickable {
                    goToWebsite(context, question.link)
                })
    }
    if (showNoteDialog) {
        NoteDialog(note = note, onDismiss = {
            showNoteDialog = false
        }) {
            viewModel.onNoteAdded(question, it)
            showNoteDialog = false
        }
    }
}

@Composable
fun NoteDialog(note: String, onDismiss: () -> Unit, onNoteAdded: (String) -> Unit) {
    var noteText by remember { mutableStateOf(note) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFBFE))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Note",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 150.dp),
                    colors = TextFieldDefaults.colors(),
                    placeholder = { Text("Enter your note here") },
                )
                Spacer(modifier = Modifier.height(16.dp))
                ElevatedButton(
                    onClick = { onNoteAdded(noteText) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTooltip(data: TooltipData, modifier: Modifier = Modifier) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(), tooltip = {
            RichTooltip(title = { Text(data.title) }) {
                Text(data.content)
            }
        }, state = tooltipState
    ) {
        IconButton(onClick = {
            scope.launch {
                if (tooltipState.isVisible) {
                    tooltipState.dismiss()
                } else {
                    tooltipState.show()
                }
            }
        }, modifier = modifier) {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Localized Description")
        }
    }
}

@Immutable
data class TooltipData(val title: String, val content: String, val weight: Float)

object DsaSheetScreenData {
    val tooltipData = listOf(
        TooltipData(
            "Solved",
            "Indicates whether the question has been solved. Check this box if you have completed the question.",
            0.15f
        ), TooltipData(
            "Title",
            "The title of the question. This provides a brief description of the problem to be solved.",
            0.50f
        ), TooltipData(
            "Revision",
            "Indicates whether the question needs to be revised. Check this box if you plan to revisit the question.",
            0.15f
        ), TooltipData(
            "Note",
            "Add a personal note or reminder about this question. Click the note icon to add or view notes.",
            0.10f
        ), TooltipData(
            "Leetcode Link",
            "A link to the question on Leetcode. Click the image to open the question in your web browser.",
            0.10f
        )
    )
}