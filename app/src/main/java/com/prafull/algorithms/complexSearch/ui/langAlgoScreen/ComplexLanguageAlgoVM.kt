package com.prafull.algorithms.complexSearch.ui.langAlgoScreen

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.complexSearch.ComplexRoutes
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageAlgo
import com.prafull.algorithms.complexSearch.domain.repo.ComplexSearchRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplexLanguageAlgoVM(
    private val data: ComplexRoutes.ComplexLanguageAlgoRoute
) : ViewModel(), KoinComponent {

    private val complexRepo: ComplexSearchRepo by inject()

    var lang by mutableStateOf("")
    var selectedAlgo by mutableStateOf("")

    init {
        selectedAlgo = data.algo
        lang = data.lang
        getProblemDetails()
    }

    // This is the algorithm which user has clicked to see particular language implementation


    // This is the state of the problem which user has clicked
    private val _problemDetails =
        MutableStateFlow<BaseClass<ComplexLanguageAlgo>>(BaseClass.Loading)
    val problemDetails = _problemDetails.asStateFlow()


    /**
     *      Get the details of the problem which user has clicked
     * */
    fun getProblemDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            complexRepo.getComplexLanguageAlgo(data.lang, data.algo).collectLatest { response ->
                _problemDetails.update { response }
            }
        }
    }

    fun isKeySaved(context: Context) =
        context.getSharedPreferences(Const.API_KEY_PREF, Context.MODE_PRIVATE)
            .getBoolean("isKeySaved", false)

}