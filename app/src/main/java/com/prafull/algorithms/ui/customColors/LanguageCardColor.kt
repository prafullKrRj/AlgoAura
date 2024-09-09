package com.prafull.algorithms.ui.customColors

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CardDefaults.langColor(): CardColors {
    return this.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
}