package com.prafull.algorithms.models

import com.prafull.algorithms.Routes

data class FileInfo(
    val id: String,
    val name: String,
    val path: String,
    val language: ProgrammingLanguage
) {
    fun toCodeScreen() = Routes.CodeScreen(
        id = id,
        name = name,
        path = path,
        langName = language.name
    )
}