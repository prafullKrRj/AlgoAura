package com.prafull.algorithms.screens.search

import androidx.lifecycle.ViewModel
import com.prafull.algorithms.data.RoomHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val roomHelper: RoomHelper
) : ViewModel() {

    private val sampleData = listOf(
        "Apple",
        "Banana",
        "Cherry",
        "Date",
        "Elderberry",
        "Fig",
        "Grape",
        "Honeydew",
        "Jackfruit",
        "Kiwi",
        "Lemon",
        "Mango",
        "Nectarine",
        "Orange",
        "Papaya",
        "Quince",
        "Raspberry",
        "Strawberry",
        "Tangerine",
        "Ugli",
        "Vanilla",
        "Watermelon",
        "Ximenia",
        "Yuzu",
        "Zucchini"
    )

    val searchResults = mutableListOf<String>()

    fun search(value: String) {
        if (value.isEmpty()) {
            searchResults.clear()
            return
        }
        searchResults.clear()
        sampleData.forEach {
            if (it.contains(value, ignoreCase = true)) {
                searchResults.add(it)
            }
        }
    }

}