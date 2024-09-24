package com.prafull.algorithms.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.goBackStack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, navController: NavController) {
    val isKeyAdded by settingsViewModel.keyAdded.collectAsState()
    val loading by settingsViewModel.loading.collectAsState()
    var showApiDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Settings")
        }, navigationIcon = {
            IconButton(onClick = navController::goBackStack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
    }) { paddingValues ->
        LazyColumn(
            Modifier.padding(paddingValues)
        ) {
            item {
                DeleteItem(category = "Favourites") {
                    settingsViewModel.deleteFavourites()

                }
            }
            item {
                DeleteItem(category = "Search History") {
                    settingsViewModel.deleteSearchHistory()
                }
            }
            item {
                if (isKeyAdded) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                showApiDialog = true
                            },
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {
                            showApiDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Create,
                                contentDescription = "Change Api Key",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(text = "Change API Key", Modifier.weight(1f))
                    }
                    DeleteItem(category = "Api Key") {
                        settingsViewModel.deleteApiKey()
                    }
                } else {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                showApiDialog = true
                            },
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {
                            showApiDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Create,
                                contentDescription = "Create Api Key",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(text = "Create API Key", Modifier.weight(1f))
                    }
                }
            }
        }
    }
    if (showApiDialog) {
        ChangeKeyDialog(
            loading = loading,
            text = "Enter your API Key",
            title = "Change API Key",
            onDismiss = {
                showApiDialog = false
            },
            viewmodel = settingsViewModel,
        )
    }
}

@Composable
fun ChangeKeyDialog(
    loading: Boolean,
    text: String,
    title: String,
    onDismiss: () -> Unit,
    viewmodel: SettingsViewModel
) {
    var apiKey by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    AlertDialog(onDismissRequest = {}, confirmButton = {
        TextButton(onClick = {
            scope.launch {
                viewmodel.changeApiKey(apiKey, context)
                onDismiss()
            }
        }, enabled = !loading) {
            Text("Confirm")
        }
    }, text = {
        Box(contentAlignment = Center) {
            Column {
                Text(text)
                OutlinedTextField(value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("API Key") })
            }
            if (loading) {
                CircularProgressIndicator()
            }
        }
    }, title = {
        Text(title)
    }, dismissButton = {
        TextButton(onClick = onDismiss, enabled = !loading) {
            Text("Cancel")
        }
    })
}

@Composable
fun DeleteItem(category: String, onDelete: () -> Unit) {
    var showWarningDialog by remember {
        mutableStateOf(false)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                showWarningDialog = true
            },
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = {
            showWarningDialog = true
        }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
        Text(text = "Delete $category", Modifier.weight(1f))
    }
    if (showWarningDialog) {
        AlertDialog(onDismissRequest = {
            showWarningDialog = false
        }, confirmButton = {
            TextButton(onClick = {
                showWarningDialog = false
                onDelete()
            }) {
                Text("Confirm")
            }
        }, text = {
            Text("Are you sure you want to delete all $category?")
        }, title = {
            Text("Delete $category")
        }, dismissButton = {
            TextButton(onClick = {
                showWarningDialog = false
            }) {
                Text("Cancel")
            }
        })
    }
}