package com.prafull.algorithms.homeScreen.folder

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.commons.ads.BannerAdView
import com.prafull.algorithms.commons.components.ErrorComposable
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.commons.utils.getFileName
import com.prafull.algorithms.commons.utils.getFormattedName
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.homeScreen.HomeRoutes
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    viewModel: FolderViewModel, folderScreen: HomeRoutes.FolderScreen, navController: NavController
) {
    val files by viewModel.files.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = folderScreen.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                })
                Image(
                    painter = painterResource(id = folderScreen.langLogo),
                    contentDescription = "langIcon",
                    modifier = Modifier.size(40.dp)
                )
            }
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
                ErrorComposable(exception = (files as BaseClass.Error).exception) {
                    viewModel.getFiles(folderScreen.path)
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
                    BannerAdView(Const.FOLDER_SCREEN_BANNER)
                    LazyColumn(
                        modifier = Modifier.padding(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(data, key = { idx, _ ->
                            idx
                        }) { _, algorithm ->
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
                                                algorithm.toCodeScreen()
                                            )
                                        }
                                        .padding(8.dp)) {
                                    Text(text = getFormattedName(getFileName(algorithm.name)))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}