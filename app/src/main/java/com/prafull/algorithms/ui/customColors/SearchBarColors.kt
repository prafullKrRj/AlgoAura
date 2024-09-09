package com.prafull.algorithms.ui.customColors

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable

@Composable
fun OutlinedTextFieldDefaults.algoColors(): TextFieldColors {
    return this.colors(
        focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
    )
}