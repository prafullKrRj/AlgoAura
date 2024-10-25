package com.prafull.algorithms.complexSearch.domain.models

data class ComplexLanguageData(
    val name: String,
    val extension: String? = "", // Optional
    val langDescription: String? = "",
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