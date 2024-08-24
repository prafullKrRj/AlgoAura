package com.prafull.algorithms.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.RoomHelper
import com.prafull.algorithms.data.local.AlgorithmEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val room: RoomHelper
) : ViewModel() {


    fun toggle(algo: AlgorithmEntity) {

    }

    val favouriteAlgorithms = room.getAllAlgorithms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(7000), emptyList())


}