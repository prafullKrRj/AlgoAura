package com.prafull.algorithms.features.ai

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.prafull.algorithms.R
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import dev.snipme.kodeview.view.CodeTextView

@Composable
fun AiMessageBubble(
    message: ChatMessage, clipboardManager: ClipboardManager, context: Context
) {
    val markdownItems = parseMarkdown(message.text)
    Column(modifier = Modifier.padding(16.dp)) {
        markdownItems.forEach { item ->
            when (item) {
                is MarkdownItem.NormalText -> {
                    val state = rememberRichTextState()
                    state.setMarkdown(item.text)
                    RichText(state = state, modifier = Modifier.padding(bottom = 8.dp))
                }

                is MarkdownItem.CodeBlock -> {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.language)
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    clipboardManager.setText(AnnotatedString(item.code))
                                    Toast.makeText(
                                        context, "Code copied to clipboard", Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_copy_all_24),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy Code")
                            }
                        }
                        CodeTextView(
                            highlights = Highlights.Builder().code(item.code).theme(
                                theme = SyntaxThemes.darcula()
                            ).build(), modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


sealed class MarkdownItem {
    data class NormalText(val text: String) : MarkdownItem()
    data class CodeBlock(val code: String, val language: String) : MarkdownItem()
}

fun parseMarkdown(markdown: String): List<MarkdownItem> {
    val markdownItems = mutableListOf<MarkdownItem>()
    val lines = markdown.lines()
    var isInCodeBlock = false
    var language = ""
    val codeBlock = StringBuilder()
    val normalText = StringBuilder()

    for (line in lines) {
        if (line.trim().startsWith("```")) {
            if (isInCodeBlock) {
                // Close the code block and add it to the list
                markdownItems.add(MarkdownItem.CodeBlock(codeBlock.toString().trim(), language))
                codeBlock.clear()
                language = ""
            } else {
                // If there's any normal text before a code block, add it to the list
                if (normalText.isNotEmpty()) {
                    markdownItems.add(MarkdownItem.NormalText(normalText.toString().trim()))
                    normalText.clear()
                }
                // Start a new code block
                val parts = line.trim().split(" ")
                language = if (parts.size > 1) parts[1] else ""
            }
            isInCodeBlock = !isInCodeBlock
        } else {
            if (isInCodeBlock) {
                codeBlock.appendLine(line)
            } else {
                normalText.appendLine(line)
            }
        }
    }

    // Add remaining normal text or code block if any
    if (normalText.isNotEmpty()) {
        markdownItems.add(MarkdownItem.NormalText(normalText.toString().trim()))
    }

    return markdownItems
}