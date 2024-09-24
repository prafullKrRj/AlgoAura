package com.prafull.algorithms.homeScreen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import coil.network.HttpException
import com.prafull.algorithms.R
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.components.ErrorComposable
import com.prafull.algorithms.commons.models.ProgrammingLanguage
import com.prafull.algorithms.commons.ui.customColors.algoCard
import com.prafull.algorithms.commons.ui.customColors.langFilterChip
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.getFormattedName
import com.prafull.algorithms.homeScreen.HomeRoutes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    val languagesPart1 =
        ProgrammingLanguage.entries.filter { it != ProgrammingLanguage.UNKNOWN }
            .take(ProgrammingLanguage.entries.size / 2)
    val languagesPart2 =
        ProgrammingLanguage.entries.filter { it != ProgrammingLanguage.UNKNOWN }
            .drop(ProgrammingLanguage.entries.size / 2)

    Scaffold(topBar = {
        TopAppBar(title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.algorithms_logo),
                    contentDescription = "Application Logo",
                    modifier = Modifier.size(32.dp),
                )
                Text(text = "AlgoAura")
            }
        }, actions = {
            IconButton(onClick = {
                navController.navigate(Routes.SettingsRoute)
            }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            }
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(languagesPart1) { programmingLanguage ->
                    val index = ProgrammingLanguage.entries.indexOf(programmingLanguage)
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
                    }, colors = FilterChipDefaults.langFilterChip()
                    )
                }
            }

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(languagesPart2) { programmingLanguage ->
                    val index = ProgrammingLanguage.entries.indexOf(programmingLanguage)
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
                    }, colors = FilterChipDefaults.langFilterChip()
                    )
                }
            }
            when (state) {
                is BaseClass.Error -> {
                    ErrorComposable(exception = (state as BaseClass.Error).exception) {
                        viewModel.getFromLanguage(ProgrammingLanguage.entries[selected.intValue])
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
                                colors = CardDefaults.algoCard()
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            navController.navigate(
                                                HomeRoutes.FolderScreen(
                                                    path = folder.path,
                                                    name = folder.name,
                                                    langLogo = ProgrammingLanguage.entries[selected.intValue].logo
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