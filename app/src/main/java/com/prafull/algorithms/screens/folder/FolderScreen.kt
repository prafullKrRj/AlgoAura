package com.prafull.algorithms.screens.folder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.ads.BannerAdView
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.getFileName
import com.prafull.algorithms.utils.getFormattedName
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    viewModel: FolderViewModel, folderScreen: Routes.FolderScreen, navController: NavController
) {
    val files by viewModel.files.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = folderScreen.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            })
        }, navigationIcon = {
            IconButton(onClick = {
                navController.goBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "To Home"
                )
            }
        })
    }) { paddingValues ->
        when (files) {
            is BaseClass.Error -> {
                val message = (files as BaseClass.Error).message
                Text(text = message)
                Button(onClick = {
                    viewModel.getFiles(folderScreen.path)
                }) {
                    Text(text = "Retry")
                }
            }

            BaseClass.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is BaseClass.Success -> {
                val data = (files as BaseClass.Success).data
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(paddingValues = paddingValues)
                ) {
                    BannerAdView()
                    LazyColumn(
                        modifier = Modifier.padding(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(data, key = {
                            "${it.path}-${it.name}"
                        }) { file ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                )
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            navController.navigate(
                                                file.toCodeScreen()
                                            )
                                        }
                                        .padding(8.dp)
                                ) {
                                    Text(text = getFormattedName(getFileName(file.name)))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}