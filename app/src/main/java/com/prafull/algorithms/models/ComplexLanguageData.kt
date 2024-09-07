package com.prafull.algorithms.models

data class ComplexLanguageData(
    val name: String,
    val extension: String,
    val langDescription: String,
    val files: List<ComplexLanguageFiles>
)

data class ComplexLanguageFiles(
    val name: String,
    val content: String
)
