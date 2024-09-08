package com.prafull.algorithms.screens.complexSearch.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.ComplexRoutes

@Composable
fun LanguageSuccess(
    navController: NavController,
    languages: List<String>
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
        modifier
            .padding(6.dp)
            .clickable {
                navController.navigate(ComplexRoutes.ComplexSearchLanguage(languageName))
            }, shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = languageName, modifier = Modifier.padding(16.dp))
    }
}
