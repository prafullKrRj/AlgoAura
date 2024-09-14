package com.prafull.algorithms.commons.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.prafull.algorithms.R

@Composable
fun AskAiChip(onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = {
            Text("Ask AI")
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ai),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        enabled = true,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    )

}