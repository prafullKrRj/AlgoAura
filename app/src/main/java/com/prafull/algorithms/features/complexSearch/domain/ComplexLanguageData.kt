package com.prafull.algorithms.features.complexSearch.domain

data class ComplexLanguageData(
    val name: String,
    val extension: String,
    val langDescription: String,
    val files: List<ComplexLanguageFiles>
)

data class ComplexLanguageFiles(
    val name: String,
)

data class ComplexLanguageAlgo(
    val algoName: String,
    val task: String,
    val langCode: List<String>
)