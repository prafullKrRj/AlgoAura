package com.prafull.algorithms.codeScreen.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.Routes
import com.prafull.algorithms.codeScreen.domain.CodeRepository
import com.prafull.algorithms.commons.models.Algorithm
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CodeViewModel(
    private val fileInfo: Routes.CodeScreen
) : ViewModel(), KoinComponent {
    private val codeRepo: CodeRepository by inject()


    var programName by mutableStateOf(fileInfo.name)
    var isFav by mutableStateOf(false)

    private val _state = MutableStateFlow<BaseClass<Algorithm>>(BaseClass.Loading)
    val state get() = _state.asStateFlow()

    var algorithm by mutableStateOf<Algorithm?>(null)

    fun isKeySaved(context: Context) =
        context.getSharedPreferences(Const.API_KEY_PREF, Context.MODE_PRIVATE)
            .getBoolean("isKeySaved", false)

    fun toggleFav() {
        isFav = !isFav
        if (algorithm != null) {
            viewModelScope.launch(Dispatchers.Default) {
                codeRepo.toggle(algorithm!!)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            isFav = codeRepo.checkIfAlgoExists(fileInfo.id)
        }
        getCode()
    }

    private fun getCode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                codeRepo.getAlgorithm(fileInfo.toFileInfo()).collectLatest { resp ->
                    _state.update {
                        resp
                    }
                    if (resp is BaseClass.Success) {
                        algorithm = resp.data
                    }
                }
            }
        }
    }

    fun addToDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (algorithm != null) {
                    codeRepo.insert(algorithm!!)
                }
            }
        }
    }
}
