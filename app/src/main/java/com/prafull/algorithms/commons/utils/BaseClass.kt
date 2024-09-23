package com.prafull.algorithms.commons.utils

sealed interface BaseClass<out T> {
    data class Error(val message: String, val exception: Exception = Exception()) :
        BaseClass<Nothing>

    data class Success<T>(val data: T) : BaseClass<T>
    data object Loading : BaseClass<Nothing>
}