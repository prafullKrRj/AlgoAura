package com.prafull.algorithms.codeScreen.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.ads.BannerAdView
import com.prafull.algorithms.commons.ads.InterstitialAdManager
import com.prafull.algorithms.commons.components.AskAiChip
import com.prafull.algorithms.commons.components.AskAiDialog
import com.prafull.algorithms.commons.components.CodeScreenBottomBar
import com.prafull.algorithms.commons.components.CodeScreenTopAppBar
import com.prafull.algorithms.commons.components.ErrorComposable
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.commons.utils.getFileName
import com.prafull.algorithms.commons.utils.getFormattedName
import com.prafull.algorithms.commons.utils.getKodeViewLanguageFromLanguage
import com.prafull.algorithms.goBackStack
import dev.snipme.highlights.Highlights
import dev.snipme.kodeview.view.CodeTextView

@Composable
fun CodeScreen(viewModel: CodeViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val onFavClick = remember {
        {
            viewModel.toggleFav()
        }
    }
    var goToAiDialogBox by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Scaffold(topBar = {
        CodeScreenTopAppBar(
            isFavorite = viewModel.isFav,
            onFavClick = onFavClick,
            onBackClick = navController::goBackStack,
            title = getFormattedName(getFileName(viewModel.programName)),
            showToggle = viewModel.algorithm != null
        )
    }, bottomBar = {
        if (viewModel.algorithm != null) {
            CodeScreenBottomBar(viewModel.algorithm!!.code)
        }
    }, floatingActionButton = {
        if (viewModel.algorithm != null) {
            AskAiChip {
                if (viewModel.isKeySaved(context)) {
                    goToAiDialogBox = true
                } else {
                    Toast.makeText(context, "Please save your API Key first", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(Routes.EnrollToAiRoute)
                }
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            BannerAdView(Const.CODE_SCREEN_BANNER)
            when (state) {
                is BaseClass.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is BaseClass.Success -> {
                    val successState = state as BaseClass.Success
                    val code = (state as BaseClass.Success).data.code

                    val highlights = remember {
                        Highlights.Builder(code = code)
                            .language(language = getKodeViewLanguageFromLanguage(successState.data.language))
                            .build()
                    }
                    OutlinedCard(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(width = 2.dp, color = Color.LightGray)
                    ) {
                        CodeTextView(
                            highlights = highlights,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        )
                    }
                }

                is BaseClass.Error -> {
                    ErrorComposable(
                        exception = (state as BaseClass.Error).exception,
                        onRetry = viewModel::getCode
                    )
                }
            }
        }
    }
    var showInterstitialAd by rememberSaveable {
        mutableStateOf(false)
    }
    var currMessage by rememberSaveable {
        mutableStateOf("")
    }
    if (goToAiDialogBox) {
        AskAiDialog(onDismiss = {
            goToAiDialogBox = false
        }) {
            goToAiDialogBox = false
            showInterstitialAd = true
            currMessage = it
        }
    }
    if (showInterstitialAd) {
        InterstitialAdManager(adUnitId = "ca-app-pub-3940256099942544/1033173712") {
            showInterstitialAd = false
            navController.navigate(
                Routes.AskAi(
                    code = viewModel.algorithm!!.code,
                    programName = viewModel.programName,
                    message = currMessage,
                    language = viewModel.algorithm!!.language.name
                )
            )
        }
    }
}