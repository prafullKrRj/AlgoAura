package com.prafull.algorithms.screens.enrollToAi.howToCreateApiKey

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prafull.algorithms.R
import com.prafull.algorithms.goBackStack
import com.prafull.algorithms.utils.BaseClass


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToCreateApiKeyScreen(navController: NavController, viewModel: HowToCreateApiKeyViewModel) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "How to create API Key")
        }, navigationIcon = {
            IconButton(onClick = navController::goBackStack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is BaseClass.Loading -> {
                    CircularProgressIndicator()
                }

                is BaseClass.Error -> {
                    ErrorScreen(errorMessage = "An error occurred. Please try again.",
                        onRetry = { viewModel.getSteps() })
                }

                is BaseClass.Success -> {
                    HowToGetApiKeySuccessScreen((state as BaseClass.Success<HowToCreateApiKeyState>).data)
                }
            }
        }
    }

}

@Composable
private fun HowToGetApiKeySuccessScreen(data: HowToCreateApiKeyState) {
    val context = LocalContext.current
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "description") {
            Text(text = "Description", style = MaterialTheme.typography.headlineMedium)
            Text(text = data.description, style = MaterialTheme.typography.bodyMedium)
        }
        itemsIndexed(data.steps, key = { idx, _ -> idx }) { idx, steps ->
            Text(
                text = "${idx + 1}. ${steps.title}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = steps.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = ImageRequest.Builder(context).data(steps.image).crossfade(true).build(),
                contentDescription = "step image",
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.ic_broken_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(100.dp, 100.dp),
            )
        }
        item {
            Button(onClick = {
                goToWebsite(context, data.url)
            }, Modifier.fillMaxWidth()) {
                Text(text = "Go to Website")
            }
        }
    }
}

fun goToWebsite(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier, errorMessage: String, onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        FilledTonalButton(onClick = onRetry) {
            Text(text = "Sorry, Retry")
        }
    }
}