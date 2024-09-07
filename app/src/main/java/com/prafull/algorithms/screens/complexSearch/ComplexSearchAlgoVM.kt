package com.prafull.algorithms.screens.complexSearch

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.firebase.FirebaseHelper
import com.prafull.algorithms.models.ComplexAlgorithm
import com.prafull.algorithms.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplexSearchAlgoVM(
    private val algoName: String
) : ViewModel(), KoinComponent {

    private val firebase by inject<FirebaseHelper>()
    private val _algoDetails = MutableStateFlow<BaseClass<ComplexAlgorithm>>(BaseClass.Loading)
    val algoDetails = _algoDetails.asStateFlow()

    var algo: String by mutableStateOf("")

    init {
        algo = algoName.replace("+", " ").replace("-", " ")
        getAlgoDetails()
    }

    private fun getAlgoDetails() {
        Log.d("ComplexSearchAlgoVM", "getAlgoDetails: $algoName")
        viewModelScope.launch(Dispatchers.IO) {
            firebase.getComplexAlgo(algoName).collectLatest { response ->
                _algoDetails.update { response }
            }
        }
    }
}