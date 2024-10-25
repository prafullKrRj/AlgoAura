package com.prafull.algorithms.commons.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.prafull.algorithms.commons.ui.customColors.algoColors

@Composable
fun CustomSearchBar(
    label: String = "Search",
    modifier: Modifier = Modifier,
    showSearchButton: Boolean = true,
    leadingIcon: ImageVector = Icons.Default.Search,
    onLeadingIconClick: () -> Unit = {},
    leadingIconEnabled: Boolean = false,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit = {}
) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = OutlinedTextFieldDefaults.algoColors(),
        leadingIcon = {
            IconButton(onClick = onLeadingIconClick, enabled = leadingIconEnabled) {
                Icon(imageVector = leadingIcon, contentDescription = "Search Icon")
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if (showSearchButton) {
                IconButton(onClick = onSearch) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Search Icon"
                    )
                }
            }
        }
    )
}