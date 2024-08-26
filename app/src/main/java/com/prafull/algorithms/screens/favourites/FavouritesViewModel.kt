package com.prafull.algorithms.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.local.AlgorithmEntity
import com.prafull.algorithms.data.room.RoomHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavouritesViewModel(
    private val room: RoomHelper
) : ViewModel() {


    fun toggle(algo: AlgorithmEntity) {

    }

    val favouriteAlgorithms = room.getAllAlgorithms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(7000), emptyList())


}