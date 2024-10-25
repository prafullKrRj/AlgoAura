package com.prafull.algorithms.dsaSheet.ui.dsa_question_Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.R
import com.prafull.algorithms.commons.components.shareCode
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.enrollToAi.howToCreateApiKey.ErrorScreen
import com.prafull.algorithms.enrollToAi.howToCreateApiKey.goToWebsite
import com.prafull.algorithms.goBackStack
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsaQuestionScreen(viewModel: DsaQuestionViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val verticalScrollState = rememberScrollState()
    Scaffold(Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = viewModel.about.question)
        }, navigationIcon = {
            IconButton(onClick = navController::goBackStack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is BaseClass.Error -> {
                    ErrorScreen(errorMessage = "Some error occurred") {
                        viewModel.getAlgoFromFirestore()
                    }
                }

                BaseClass.Loading -> {
                    CircularProgressIndicator()
                }

                is BaseClass.Success -> {
                    SuccessScreen((state as BaseClass.Success).data)
                }
            }
        }
    }
}


@Composable
private fun SuccessScreen(questionAbout: QuestionAbout) {
    var selected by rememberSaveable { mutableStateOf(SyntaxLanguage.CPP) }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(modifier = Modifier.weight(.5f),
            selected = selected == SyntaxLanguage.CPP,
            onClick = {
                selected = SyntaxLanguage.CPP
            },
            label = {
                Text(text = "C++")
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.c_plus_plus),
                    contentDescription = "C++",
                    modifier = Modifier.size(24.dp)
                )
            })
        FilterChip(modifier = Modifier.weight(.5f),
            selected = selected == SyntaxLanguage.JAVA,
            onClick = {
                selected = SyntaxLanguage.JAVA
            },
            label = {
                Text(text = "Java")
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.java), contentDescription = "Java",
                    modifier = Modifier.size(24.dp)
                )
            })
    }
    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = LightGray,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        item {
            if (selected == SyntaxLanguage.JAVA) CodeTextView(
                highlights = Highlights.Builder(code = questionAbout.solutionJava).theme(
                    SyntaxThemes.darcula()
                ).language(language = SyntaxLanguage.JAVA).build(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            else {
                CodeTextView(
                    highlights = Highlights.Builder(code = questionAbout.solutionCpp).theme(
                        SyntaxThemes.darcula()
                    ).language(language = SyntaxLanguage.CPP).build(), modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
        }
    }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "To Leetcode",
            Modifier
                .size(24.dp)
                .clickable {
                    goToWebsite(context, questionAbout.link)
                }
        )
        IconButton(onClick = {
            shareCode(
                code = if (selected == SyntaxLanguage.JAVA) questionAbout.solutionJava
                else questionAbout.solutionCpp,
                context = context
            )
        }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                Modifier.size(24.dp)
            )
        }
        IconButton(onClick = {
            clipboard.setText(
                AnnotatedString(
                    text = if (selected == SyntaxLanguage.JAVA) questionAbout.solutionJava
                    else questionAbout.solutionCpp
                )
            )
            Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_content_copy_24),
                contentDescription = "Copy",
                Modifier.size(24.dp)
            )
        }
    }
}