package com.prafull.algorithms.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.features.complexSearch.domain.ComplexAlgorithm
import com.prafull.algorithms.features.complexSearch.domain.ComplexLanguageAlgo
import com.prafull.algorithms.features.complexSearch.domain.ComplexLanguageData
import com.prafull.algorithms.features.complexSearch.domain.ComplexLanguageFiles
import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.models.FolderInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.Const
import com.prafull.algorithms.utils.getFileName
import com.prafull.algorithms.utils.getFormattedNameExtension
import com.prafull.algorithms.utils.getLanguageFromString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.File

class FirebaseHelperImpl(
    private val storage: FirebaseStorage, private val db: FirebaseFirestore
) : FirebaseHelper {

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

    // get algorithms into a particular folder in home screen like C++ -> Bit Manipulation -> algos -> particular algorithm
    override fun getAlgorithm(fileInfo: FileInfo): Flow<BaseClass<Algorithm>> {
        return callbackFlow {
            trySend(BaseClass.Loading)
            try {
                val language = getLanguageFromString(fileInfo.name)
                val pathDetails = fileInfo.path.split("/")
                val document = db.collection(Const.LANGUAGES).document(pathDetails[0])
                    .collection(pathDetails[1]).document(fileInfo.name).get().await()
                val algo = Algorithm(
                    id = document.get("id") as String,
                    code = document.get("content") as String,
                    language = language,
                    title = getFileName(document.id).getFormattedNameExtension(),
                    langName = language.languageGenerics,
                    extension = language.extension
                )
                trySend(
                    BaseClass.Success(algo)
                )
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "An error occurred"))
            }
            awaitClose { }
        }.flowOn(Dispatchers.IO)
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

    // get list of documents from algos collection for search screen
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

    override suspend fun getComplexLanguages(): Flow<BaseClass<List<String>>> {
        return callbackFlow {
            try {
                val response = db.collection("rosettaLists").document("languages").get().await()
                trySend(
                    BaseClass.Success(
                        response.get("names") as List<String>
                    )
                )
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error"))
            }
            awaitClose { }
        }
    }

    override suspend fun getComplexSearchResults(query: String): Flow<BaseClass<List<String>>> {
        return callbackFlow {
            try {
                val normalizedQuery = query.replace("_", " ").lowercase()
                val regexQuery = Regex(".*${normalizedQuery.replace(" ", ".*")}.*")

                val document = db.collection("rosettaLists").document("algos").get().await()
                val names = document.get("names") as List<String>
                val matchingNames = names.filter { name ->
                    regexQuery.matches(
                        name.lowercase().replace("+", " ").replace("-", " ")
                    )
                }

                trySend(BaseClass.Success(matchingNames))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error", exception = e))
            }
            awaitClose { }
        }
    }

    override suspend fun getComplexAlgo(algoName: String): Flow<BaseClass<ComplexAlgorithm>> {
        return callbackFlow {
            try {
                Log.d("Bugger", algoName)
                val document = db.collection("rosettaAlgos").document(algoName).get().await()
                val algo = ComplexAlgorithm(
                    name = document.get("name") as String,
                    id = document.id,
                    task = document.get("task") as String,
                    languages = document.get("languages") as List<String>,
                )
                trySend(BaseClass.Success(algo!!))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error", exception = e))
            }

            awaitClose { }
        }
    }

    override suspend fun getComplexLanguageData(lang: String): Flow<BaseClass<ComplexLanguageData>> {
        return callbackFlow {
            try {
                val doc = db.collection("rosettalang").document(lang).get().await()
                val data = ComplexLanguageData(name = doc.id,
                    extension = doc.get("extension") as String,
                    langDescription = doc.get("language") as String,
                    files = (doc.get("algos") as List<String>).map {
                        ComplexLanguageFiles(
                            name = it
                        )
                    })
                trySend(BaseClass.Success(data))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error", exception = e))
            }

            awaitClose { }
        }
    }

    override suspend fun getComplexLanguageAlgo(
        lang: String, algo: String
    ): Flow<BaseClass<ComplexLanguageAlgo>> {
        return callbackFlow {
            try {
                val doc = db.collection("rosettaAlgos").document(algo).get().await()
                val task = doc.getString("task")
                Log.d("Bugger", "Path: Task/$algo/$lang")
                val stRef = storage.reference.child("Task").child(algo).child(lang)
                val codes = stRef.listAll().await()
                val res = codes.items.map {
                    val localFile = File.createTempFile("tempFile", null)
                    it.getFile(localFile).await()
                    Log.d("Bugger", "File: ${localFile.extension}")
                    localFile.readText()
                }
                trySend(
                    BaseClass.Success(
                        ComplexLanguageAlgo(
                            algoName = algo, task = task ?: "", langCode = res
                        )
                    )
                )
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error", exception = e))
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