package com.prafull.algorithms.commons

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AskAiChip(onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(text = "Ask AI")
        }, colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ), leadingIcon = {
            Text(text = "✨")
        },
        border = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.tertiaryContainer)
    )
}