package com.prafull.algorithms.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.getFormattedName

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: AlgoViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }
    var switched by rememberSaveable {
        mutableStateOf(false)
    }
    if (!switched && state is BaseClass.Loading) {
        viewModel.getFromLanguage(ProgrammingLanguage.entries[selected.intValue])
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "AlgoAura")
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProgrammingLanguage.entries.filter {
                    it != ProgrammingLanguage.UNKNOWN
                }.forEachIndexed { index, programmingLanguage ->
                    FilterChip(selected = index == selected.intValue, onClick = {
                        selected.intValue = index
                        switched = true
                        viewModel.getFromLanguage(programmingLanguage)
                    }, label = {
                        Text(text = programmingLanguage.languageGenerics)
                    }, trailingIcon = {
                        Image(
                            painter = painterResource(id = programmingLanguage.logo),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    })
                }
            }
            when (state) {
                is BaseClass.Error -> {
                    val message = (state as BaseClass.Error).message
                    Text(text = message)
                    Button(onClick = {
                        viewModel.getFromLanguage(ProgrammingLanguage.entries[selected.intValue])
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
                    val data = (state as BaseClass.Success).data
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(data, key = {
                            "${it.path}-${it.name}"
                        }) { folder ->
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
                                                Routes.FolderScreen(
                                                    path = folder.path, name = folder.name
                                                )
                                            )
                                        }
                                        .padding(8.dp)) {
                                    Text(text = getFormattedName(folder.name))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}