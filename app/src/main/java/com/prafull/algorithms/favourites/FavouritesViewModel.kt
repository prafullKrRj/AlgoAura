package com.prafull.algorithms.favourites

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.favourites.data.local.AlgorithmEntity
import com.prafull.algorithms.favourites.domain.FavRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavouritesViewModel : ViewModel(), KoinComponent {

    private val favRepo by inject<FavRepo>()

    var isFav by mutableStateOf(true)

    fun toggle(algo: AlgorithmEntity) {
        isFav = !isFav
        viewModelScope.launch {
            favRepo.toggle(algo.toAlgorithms())
        }
    }

    fun deleteSelectedAlgos(selectedAlgos: List<AlgorithmEntity>) {
        viewModelScope.launch {
            favRepo.deleteAlgos(selectedAlgos)
        }
    }

    fun isKeySaved(context: Context) =
        context.getSharedPreferences(Const.API_KEY_PREF, Context.MODE_PRIVATE)
            .getBoolean("isKeySaved", false)

    val favouriteAlgorithms = favRepo.getAllAlgorithms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(7000), emptyList())

}