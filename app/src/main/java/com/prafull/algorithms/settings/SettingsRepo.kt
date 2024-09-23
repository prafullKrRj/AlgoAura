package com.prafull.algorithms.settings

import com.prafull.algorithms.favourites.data.local.FavAlgoDao
import com.prafull.algorithms.search.data.local.SearchDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsRepo : KoinComponent {
    private val favDao: FavAlgoDao by inject()
    private val searchDao: SearchDao by inject()

    suspend fun deleteAllFav() = favDao.deleteAllFav()

    suspend fun deleteSearchHistory() = searchDao.deleteAllSearched()

}