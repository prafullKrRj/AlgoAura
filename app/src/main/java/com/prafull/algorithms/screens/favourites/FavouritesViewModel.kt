package com.prafull.algorithms.screens.favourites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.local.AlgorithmEntity
import com.prafull.algorithms.data.room.RoomHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavouritesViewModel : ViewModel(), KoinComponent {

    private val room: RoomHelper by inject()
    var isFav by mutableStateOf(true)

    fun toggle(algo: AlgorithmEntity) {
        isFav = !isFav
        viewModelScope.launch {
            room.toggle(algo.toAlgorithms())
        }
    }

    val favouriteAlgorithms = room.getAllAlgorithms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(7000), emptyList())

}