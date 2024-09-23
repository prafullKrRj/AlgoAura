package com.prafull.algorithms.homeScreen.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.models.FolderInfo
import com.prafull.algorithms.commons.models.ProgrammingLanguage
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.commons.utils.getLanguageFromString
import com.prafull.algorithms.homeScreen.domain.HomeRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HomeRepoImpl(
    private val storage: FirebaseStorage, private val db: FirebaseFirestore
) : HomeRepo {

    // get algorithms into a particular folder in home screen like C++ -> Bit Manipulation -> algos
    override fun getAlgorithms(path: String): Flow<BaseClass<List<FileInfo>>> {
        return callbackFlow {
            try {
                Log.d("FirebaseHelper", "Path: $path")
                val pathDetails = path.split("/")
                val doc = db.collection(Const.LANGUAGES).document(pathDetails[0])
                    .collection(pathDetails[1]).document("algos").get().await()
                val list = doc.get("algos") as List<Map<String, String>>
                val fileInfoList = list.map {
                    FileInfo(
                        id = it["id"] ?: "",
                        name = it["name"] ?: "",
                        path = path + "/" + it["name"],
                        language = getLanguageFromString(it["name"] ?: "")
                    )
                }
                trySend(BaseClass.Success(fileInfoList))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "An error occurred"))
            }
            awaitClose { }
        }
    }


    override fun getAlgoGroups(language: ProgrammingLanguage): Flow<BaseClass<List<FolderInfo>>> {
        return callbackFlow {
            try {
                val docRef =
                    db.collection(Const.LANGUAGES).document(language.fileName).get().await()
                val languageResponse = LanguageResponse(
                    name = docRef.getString("name") ?: "",
                    algos = docRef.get("folders") as List<String>,
                    extension = docRef.getString("extension") ?: "",
                )
                val folderInfoList = languageResponse.algos?.map { algo ->
                    FolderInfo(name = algo, path = "${language.fileName}/$algo")
                } ?: emptyList()
                trySend(BaseClass.Success(folderInfoList))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "An error occurred"))
            }

            awaitClose { }
        }
    }
}

data class LanguageResponse(
    val name: String,
    val algos: List<String>?,
    val extension: String,
)