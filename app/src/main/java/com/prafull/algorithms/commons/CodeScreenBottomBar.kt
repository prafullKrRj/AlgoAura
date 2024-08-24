package com.prafull.algorithms.commons

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.prafull.algorithms.R

@Composable
fun CodeScreenBottomBar(code: String) {
    val context = LocalContext.current
    val clipBoard = LocalClipboardManager.current
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            FloatingActionButton(onClick = {
                shareCode(code, context)
            }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }
            FloatingActionButton(onClick = {
                clipBoard.setText(AnnotatedString(code))
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_copy_all_24),
                    contentDescription = null
                )
            }
        }
    }
}

fun shareCode(code: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, code)
    val shareIntent = Intent.createChooser(intent, null)
    context.startActivity(shareIntent)
}