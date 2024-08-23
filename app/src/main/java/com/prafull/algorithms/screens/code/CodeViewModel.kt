package com.prafull.algorithms.screens.code

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CodeViewModel : ViewModel() {

    private var path by mutableStateOf("")


    fun getCode() {

    }
    fun setPath(path: String) {
        this.path = path;
    }
}
