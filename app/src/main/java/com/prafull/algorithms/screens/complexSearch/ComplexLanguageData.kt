package com.prafull.algorithms.screens.complexSearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.models.ComplexLanguageData
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplexLanguageViewModel(
    private val lang: String
) : ViewModel(), KoinComponent {
    private val firebase by inject<FirebaseHelper>()
    var langName: String by mutableStateOf("")

    private val _state = MutableStateFlow<BaseClass<ComplexLanguageData>>(BaseClass.Loading)
    val state = _state.asStateFlow()

    init {
        langName = lang
        getLangData()
    }

    private fun getLangData() {
        viewModelScope.launch(Dispatchers.IO) {
            firebase.getComplexLanguageData(lang).collectLatest { response ->
                _state.update { response }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexLanguageData(viewModel: ComplexLanguageViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = viewModel.langName)
            }, navigationIcon = {
                IconButton(onClick = navController::goBackStack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        }
    ) { paddingValues ->
        when (state) {
            is BaseClass.Error -> {
                Text(text = (state as BaseClass.Error).message)
            }

            BaseClass.Loading -> {
                Text(text = "Loading")
            }

            is BaseClass.Success -> {
                ComplexLanguageSuccess(
                    data = (state as BaseClass.Success<ComplexLanguageData>).data,
                    paddingValues
                )
            }
        }
    }
}

@Composable
fun ComplexLanguageSuccess(data: ComplexLanguageData, paddingValues: PaddingValues) {
    val textState = rememberRichTextState()
    textState.setMarkdown(data.langDescription)
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(paddingValues), contentPadding = PaddingValues(8.dp),

        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            RichText(state = textState)
        }
        items(data.files) {
            Text(text = it.name + " : " + it.content)
        }
    }
}