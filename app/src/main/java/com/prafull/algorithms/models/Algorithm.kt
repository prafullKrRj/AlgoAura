package com.prafull.algorithms.models

import com.prafull.algorithms.data.local.algo.AlgorithmEntity


data class Algorithm(
    val id: String,
    val code: String,
    val language: ProgrammingLanguage,
    val langName: String = language.fileName,
    val extension: String = language.extension,
    val title: String
) {
    fun toEntity(): AlgorithmEntity {
        return AlgorithmEntity(
            id = id,
            code = code,
            language = language.name,
            extension = language.extension,
            title = title
        )
    }
}

