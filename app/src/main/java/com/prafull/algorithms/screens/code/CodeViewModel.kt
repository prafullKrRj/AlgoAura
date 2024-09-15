package com.prafull.algorithms.screens.code

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.Routes
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.data.room.RoomHelper
import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.utils.BaseClass
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
    private val room: RoomHelper by inject()
    private val firebaseHelper: FirebaseHelper by inject()

    var programName by mutableStateOf(fileInfo.name)
    var isFav by mutableStateOf(false)

    private val _state = MutableStateFlow<BaseClass<Algorithm>>(BaseClass.Loading)
    val state get() = _state.asStateFlow()

    var algorithm by mutableStateOf<Algorithm?>(null)

    fun toggleFav() {
        isFav = !isFav
        if (algorithm != null) {
            viewModelScope.launch(Dispatchers.Default) {
                room.toggle(algorithm!!)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            isFav = room.checkIfAlgoExists(fileInfo.id)
        }
        getCode()
    }

    private fun getCode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseHelper.getAlgorithm(fileInfo.toFileInfo()).collectLatest { resp ->
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
                    room.insert(algorithm!!)
                }
            }
        }
    }
}
