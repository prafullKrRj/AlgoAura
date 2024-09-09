package com.prafull.algorithms.ui.customColors

import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable

@Composable
fun FilterChipDefaults.langFilterChip(): SelectableChipColors {
    return this.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
        selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
    )
}