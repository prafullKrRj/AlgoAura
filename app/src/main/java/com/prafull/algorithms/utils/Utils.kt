package com.prafull.algorithms.utils

import com.prafull.algorithms.models.ProgrammingLanguage
import dev.snipme.highlights.model.SyntaxLanguage

fun getLanguageFromString(str: String): ProgrammingLanguage {
    val extension = str.removeSuffix(".md").split(".").last()
    ProgrammingLanguage.entries.forEach { lang ->
        if (lang.extension == extension) {
            return lang
        }
    }
    return ProgrammingLanguage.UNKNOWN
}

fun getFileName(name: String): String {
    return name.removeSuffix(".md").split(".").first()
}

fun getFormattedName(input: String): String {
    return input.replace(Regex("[_-]"), " ").split(" ")
        .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
}

fun getAlgoNameFromCompletePath(path: String): String {
    return getFormattedName(getFileName(path.split("/").last()))
}


fun getKodeViewLanguageFromLanguage(language: ProgrammingLanguage): SyntaxLanguage {
    return when (language) {
        ProgrammingLanguage.C_PLUS_PLUS -> SyntaxLanguage.CPP
        ProgrammingLanguage.C_SHARP -> SyntaxLanguage.CSHARP
        ProgrammingLanguage.C -> SyntaxLanguage.C
        ProgrammingLanguage.DART -> SyntaxLanguage.MIXED
        ProgrammingLanguage.JAVA -> SyntaxLanguage.JAVA
        ProgrammingLanguage.JAVASCRIPT -> SyntaxLanguage.JAVASCRIPT
        ProgrammingLanguage.JULIA -> SyntaxLanguage.MIXED
        ProgrammingLanguage.KOTLIN -> SyntaxLanguage.KOTLIN
        ProgrammingLanguage.PHP_SORT -> SyntaxLanguage.MIXED
        ProgrammingLanguage.PYTHON -> SyntaxLanguage.PYTHON
        ProgrammingLanguage.UNKNOWN -> SyntaxLanguage.MIXED
    }
}