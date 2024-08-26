package com.prafull.algorithms.screens.code

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.data.room.RoomHelper
import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.getAlgoNameFromCompletePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeViewModel(
    private val room: RoomHelper,
    private val firebaseHelper: FirebaseHelper
) : ViewModel() {

    private var path by mutableStateOf("")
    var programName by mutableStateOf("")
    private val _state = MutableStateFlow<BaseClass<Algorithm>>(BaseClass.Loading)
    val state get() = _state.asStateFlow()

    var algorithm by mutableStateOf<Algorithm?>(null)

    private fun getCode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseHelper.getAlgorithm(path).collectLatest { resp ->
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

    fun addPath(path: String) {
        this.path = path
        getCode()
        programName = getAlgoNameFromCompletePath(path)
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
