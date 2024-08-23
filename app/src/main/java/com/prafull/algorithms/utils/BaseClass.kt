package com.prafull.algorithms.utils

sealed interface BaseClass<out T> {
    data class Error(val message: String) : BaseClass<Nothing>
    data class Success<T>(val data: T) : BaseClass<T>
    data object Loading : BaseClass<Nothing>
}