package com.prafull.algorithms.commons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeScreenTopAppBar(
    isFavorite: Boolean,
    onFavClick: () -> Unit,
    onBackClick: () -> Unit,
    title: String,
    showToggle: Boolean
) {
    TopAppBar(
        title = { Text(text = "\uD83E\uDDD1\u200D\uD83D\uDCBB $title") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            if (showToggle) {
                IconButton(onClick = onFavClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Save to Db"
                    )
                }
            }
        })
}