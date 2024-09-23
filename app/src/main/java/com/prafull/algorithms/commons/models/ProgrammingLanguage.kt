package com.prafull.algorithms.commons.models

import androidx.annotation.DrawableRes
import com.prafull.algorithms.R


enum class ProgrammingLanguage(
    val extension: String,
    val fileName: String,
    val languageGenerics: String,
    @DrawableRes val logo: Int
) {
    C_PLUS_PLUS("cpp", "C-Plus-Plus", "C++", R.drawable.c_plus_plus), C_SHARP(
        "cs",
        "C-Sharp",
        "C#",
        R.drawable.c_sharp
    ),
    C("c", "C", "C", R.drawable.c), DART("dart", "Dart", "Dart", R.drawable.dart), JAVA(
        "java",
        "Java",
        "Java",
        R.drawable.java
    ),
    JAVASCRIPT("js", "JavaScript", "JavaScript", R.drawable.javascript), JULIA(
        "jl",
        "Julia",
        "Julia",
        R.drawable.julia
    ),
    KOTLIN("kt", "Kotlin", "Kotlin", R.drawable.kotlin), PHP_SORT(
        "php",
        "PHP",
        "PHP",
        R.drawable.php
    ),
    PYTHON("py", "Python", "Python", R.drawable.python),
    UNKNOWN("unknown", "Unknown", "Unknown", R.drawable.ic_launcher_background);
}