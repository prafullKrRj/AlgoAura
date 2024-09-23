package com.prafull.algorithms.search.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.getLanguageFromString
import com.prafull.algorithms.search.data.local.SearchDao
import com.prafull.algorithms.search.data.local.SearchedEntity
import com.prafull.algorithms.search.domain.SearchRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchRepoImpl : SearchRepo, KoinComponent {
    private val dao: SearchDao by inject()
    private val db: FirebaseFirestore by inject()

    override suspend fun insertSearchedText(searchedEntity: SearchedEntity) =
        dao.insertSearchedText(searchedEntity)

    override fun getSearchedAlgorithms(): Flow<List<SearchedEntity>> = dao.getAllSearched()

    override suspend fun getListOfDocuments(query: String): Flow<BaseClass<List<FileInfo>>> {
        return callbackFlow {
            try {
                trySend(BaseClass.Loading)

                val normalizedQuery = query.replace("_", " ").lowercase()
                val regexQuery = Regex(".*${normalizedQuery.replace(" ", ".*")}.*")

                val response = db.collection("Algorithms").document("algos").get().await()
                val list = response.get("algos") as List<Map<String, Any>>
                Log.d("FirebaseHelper", "List: $list")
                val fileInfoList = list.map {
                    FileInfo(
                        id = it["id"] as String,
                        name = it["name"] as String,
                        path = it["path"] as String,
                        language = getLanguageFromString(it["name"] as String)
                    )
                }.filter {
                    regexQuery.matches(it.name.lowercase().replace("+", " ").replace("-", " "))
                }
                trySend(BaseClass.Success(fileInfoList))
            } catch (e: Exception) {
                Log.d("FirebaseHelper", "Error: ${e.message}")
                trySend(BaseClass.Error(e.message ?: "An error occurred"))
            }

            awaitClose { }
        }
    }
}