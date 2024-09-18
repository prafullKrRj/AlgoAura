package com.prafull.algorithms.features.complexSearch.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.ComplexRoutes
import com.prafull.algorithms.R
import com.prafull.algorithms.ui.customColors.langColor

@Composable
fun LanguageSuccess(
    navController: NavController,
    languages: List<String>
) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        LanguageCard(languages[0], navController, Modifier.weight(.5f))
        if (languages.size > 1) {
            LanguageCard(languages[1], navController, Modifier.weight(.5f))
        }
    }
}


@Composable
fun LanguageCard(languageName: String, navController: NavController, modifier: Modifier) {
    Card(
        modifier, shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.langColor()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(ComplexRoutes.ComplexSearchLanguage(languageName))
            }
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Text(text = languageName, Modifier.weight(.8f))
            Image(
                painter = painterResource(id = getIcon(languageName)),
                contentDescription = null,
                modifier = Modifier.weight(.2f)
            )
        }
    }
}

fun getIcon(langName: String): Int {
    return when (langName) {
        "C++" -> R.drawable.c_plus_plus
        "Java" -> R.drawable.java
        "Python" -> R.drawable.python
        "JavaScript" -> R.drawable.javascript
        "C-sharp" -> R.drawable.c_sharp
        "PHP" -> R.drawable.php
        "Julia" -> R.drawable.julia
        "Dart" -> R.drawable.dart
        "Kotlin" -> R.drawable.kotlin
        "C" -> R.drawable.c
        else -> R.drawable.coding
    }
}