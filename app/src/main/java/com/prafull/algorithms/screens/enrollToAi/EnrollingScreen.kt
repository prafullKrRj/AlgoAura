package com.prafull.algorithms.screens.enrollToAi

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.ai.client.generativeai.GenerativeModel
import com.prafull.algorithms.R
import com.prafull.algorithms.Routes
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.utils.Const
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

// UI Component
@Composable
fun EnrollingScreen(viewModel: ApiKeyViewModel, navController: NavHostController) {
    var apiKey by remember { mutableStateOf("") }
    var isKeyVisible by remember { mutableStateOf(false) }
    val loading by viewModel.loading.collectAsState()
    val keyAdded by viewModel.keyAdded.collectAsState()

    LaunchedEffect(key1 = keyAdded) {
        if (keyAdded) {
            navController.goBackStack()
        }
    }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = "Enter your API Key", style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("API Key") },
            visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isKeyVisible = !isKeyVisible }) {
                    Icon(
                        painter = painterResource(id = if (isKeyVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                        contentDescription = "Toggle key visibility"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter Your Gemini API key is stored securely on your device and is not shared with anyone.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.saveApiKey(apiKey, context) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text("Save API Key")
        }
        FilledTonalButton(modifier = Modifier.fillMaxWidth(), onClick = {
            navController.navigate(Routes.HowToCreateApiKey)
        }) {
            Text(text = "How to create API Key?")
        }
    }
}

// ViewModel
class ApiKeyViewModel(
    context: Context
) : ViewModel(), KoinComponent {
    private val pref = context.getSharedPreferences("api_key", Context.MODE_PRIVATE)

    private val _keyAdded = MutableStateFlow(false)
    val keyAdded = _keyAdded.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun saveApiKey(apiKey: String, context: Context) {
        _loading.update { true }
        viewModelScope.launch {
            verifyApiKey(apiKey).collectLatest {
                Toast.makeText(
                    context,
                    if (it) "API Key saved successfully" else "Invalid API Key",
                    Toast.LENGTH_SHORT
                ).show()
                if (!it) {
                    _loading.update { false }
                    return@collectLatest
                }
            }
            pref.apply {
                edit().putBoolean("isKeySaved", true).apply()
                edit().putString(Const.PREF_KEY, apiKey).apply()
            }
            _keyAdded.update { true }
            _loading.update {
                false
            }
        }
    }

    private fun verifyApiKey(key: String): Flow<Boolean> = flow {
        try {
            GenerativeModel(
                apiKey = key, modelName = "gemini-1.5-flash"
            ).generateContent("Say Hi")
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }
}