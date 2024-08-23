package com.prafull.algorithms.models

import com.prafull.algorithms.data.local.AlgorithmEntity


data class Algorithms(
    val code: String,
    val language: ProgrammingLanguage,
    val langName: String = language.fileName,
    val extension: String = language.extension,
    val title: String
) {
    fun toEntity(): AlgorithmEntity {
        return AlgorithmEntity(
            code = code,
            language = language.name,
            extension = language.extension,
            title = title
        )
    }
}

