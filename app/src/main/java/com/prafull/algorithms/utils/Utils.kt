package com.prafull.algorithms.utils

import com.prafull.algorithms.models.ProgrammingLanguage

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