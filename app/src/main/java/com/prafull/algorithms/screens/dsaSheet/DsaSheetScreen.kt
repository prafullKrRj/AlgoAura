package com.prafull.algorithms.screens.dsaSheet

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prafull.algorithms.R
import com.prafull.algorithms.data.local.questions.QuestionEntity
import com.prafull.algorithms.data.local.questions.TopicWithQuestions
import com.prafull.algorithms.screens.enrollToAi.howToCreateApiKey.goToWebsite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsaSheetScreen(viewModel: DsaSheetViewModel) {
    val topics by viewModel.topicWithQuestions.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "📃 DSA Sheet", style = MaterialTheme.typography.headlineSmall)
            })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(topics, key = { idx, _ -> idx }) { idx, topicWithQuestions ->
                TopicCard(idx, topicWithQuestions, context, viewModel)
            }
        }
    }

}

@Composable
fun TopicCard(
    idx: Int, topicWithQuestions: TopicWithQuestions, context: Context, viewModel: DsaSheetViewModel
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
        Column(modifier = Modifier
            .clickable { expanded = !expanded }
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = topicWithQuestions.topic.topicName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(.85f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painter = painterResource(id = if (expanded) R.drawable.baseline_expand_less_24 else R.drawable.baseline_expand_more_24),
                        contentDescription = "Expand",
                        modifier = Modifier
                            .rotate(rotationState)
                            .weight(.15f),
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = topicWithQuestions.topic.topicDetails,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Header Row for descriptions
                    HeaderRow()

                    Spacer(modifier = Modifier.height(8.dp))

                    topicWithQuestions.questions.forEach { question ->
                        QuestionRow(question, context, viewModel)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderRow() {
}

@Composable
fun QuestionRow(question: QuestionEntity, context: Context, viewModel: DsaSheetViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Solved checkbox
        Checkbox(
            checked = question.solved, onCheckedChange = { isChecked ->
                //  viewModel.onSolvedChanged(question, isChecked)  TODO: Implement solved action
            }, modifier = Modifier.weight(0.15f), colors = CheckboxDefaults.colors()
        )

        // Title
        Text(
            text = question.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.50f)
        )

        Checkbox(
            checked = question.revision, onCheckedChange = { isChecked ->
                // viewModel.onRevisionChanged(question, isChecked) TODO: Implement revision action
            }, modifier = Modifier.weight(0.15f), colors = CheckboxDefaults.colors()
        )

        IconButton(
            onClick = { /* TODO: Implement note action */ }, modifier = Modifier.weight(0.10f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_note_add_24),
                contentDescription = "Note"
            )
        }

        AsyncImage(
            model = ImageRequest.Builder(context).data(R.drawable.leetcode).build(),
            contentDescription = "leetcode_image",
            alpha = 1f,
            modifier = Modifier
                .weight(.10f)
                .size(24.dp)
                .clickable {
                    goToWebsite(context, question.link)
                }
        )
    }
}
