package com.prafull.algorithms.settings.libraries

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.goBackStack

object Libraries {

    val librariesList = listOf(
        "Firebase Firestore" to "https://firebase.google.com/docs/firestore",
        "Firebase App Check Play Integrity" to "https://firebase.google.com/docs/app-check",
        "AndroidX Room Runtime" to "https://developer.android.com/jetpack/androidx/releases/room",
        "Firebase Crashlytics" to "https://firebase.google.com/docs/crashlytics",
        "AndroidX Room Compiler" to "https://developer.android.com/jetpack/androidx/releases/room",
        "AndroidX Room KTX" to "https://developer.android.com/jetpack/androidx/releases/room",
        "Kotlin Standard Library" to "https://kotlinlang.org/api/latest/jvm/stdlib/",
        "AndroidX Core KTX" to "https://developer.android.com/jetpack/androidx/releases/core",
        "AndroidX Lifecycle Runtime KTX" to "https://developer.android.com/jetpack/androidx/releases/lifecycle",
        "AndroidX Activity Compose" to "https://developer.android.com/jetpack/androidx/releases/activity",
        "AndroidX Compose BOM" to "https://developer.android.com/jetpack/compose/bom",
        "AndroidX UI" to "https://developer.android.com/jetpack/compose/ui",
        "AndroidX UI Graphics" to "https://developer.android.com/jetpack/compose/ui",
        "AndroidX UI Tooling Preview" to "https://developer.android.com/jetpack/compose/tooling",
        "AndroidX Material3" to "https://developer.android.com/jetpack/androidx/releases/material3",
        "Firebase Storage" to "https://firebase.google.com/docs/storage",
        "JUnit" to "https://junit.org/junit5/",
        "AndroidX JUnit" to "https://developer.android.com/jetpack/androidx/releases/test",
        "AndroidX Espresso Core" to "https://developer.android.com/training/testing/espresso",
        "AndroidX UI Test JUnit4" to "https://developer.android.com/jetpack/compose/testing",
        "AndroidX UI Tooling" to "https://developer.android.com/jetpack/compose/tooling",
        "AndroidX UI Test Manifest" to "https://developer.android.com/jetpack/compose/testing",
        "AndroidX Lifecycle ViewModel Compose" to "https://developer.android.com/jetpack/compose/lifecycle",
        "AndroidX Navigation Compose" to "https://developer.android.com/jetpack/compose/navigation",
        "Kotlinx Serialization JSON" to "https://github.com/Kotlin/kotlinx.serialization",
        "RichEditor Compose" to "https://github.com/MohamedRejeb/compose-rich-editor",
        "KodeView" to "https://github.com/SnipMeDev/KodeView",
        "Generative AI" to "https://ai.google.dev/",
        "Koin Android" to "https://insert-koin.io/docs/reference/koin-android/start",
        "Koin AndroidX Compose" to "https://insert-koin.io/docs/reference/koin-android/compose",
        "Koin Core" to "https://insert-koin.io/docs/reference/koin-core/start",
        "Koin AndroidX Navigation" to "https://insert-koin.io/docs/reference/koin-android/navigation",
        "Compose Shimmer" to "https://github.com/valentinilk/compose-shimmer",
        "Play Services Ads" to "https://developers.google.com/admob/android/quick-start",
        "Coil Compose" to "https://coil-kt.github.io/coil/compose/"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Libraries") },
                navigationIcon = {
                    IconButton(
                        onClick = navController::goBackStack
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(Libraries.librariesList) { library ->
                ListItem(headlineContent = {
                    Text(library.first)
                }, Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(library.second))
                    context.startActivity(intent)
                })
            }
        }
    }
}