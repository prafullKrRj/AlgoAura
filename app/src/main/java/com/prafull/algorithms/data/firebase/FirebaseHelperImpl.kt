package com.prafull.algorithms.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.models.Algorithm
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.models.FolderInfo
import com.prafull.algorithms.models.ProgrammingLanguage
import com.prafull.algorithms.utils.BaseClass
import com.prafull.algorithms.utils.Const
import com.prafull.algorithms.utils.getFileName
import com.prafull.algorithms.utils.getFormattedName
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

    override fun getAlgorithms(path: String): Flow<BaseClass<List<FileInfo>>> {
        return callbackFlow {
            try {
                val pathDetails = path.split("/")
                Log.d("FirebaseHelper", "Path: $pathDetails")
                val docRef = db.collection(Const.LANGUAGES).document(pathDetails[0])
                    .collection(pathDetails[1]).get().await()
                val fileInfoList = docRef.documents.map { doc ->
                    FileInfo(
                        id = doc.get("id") as String,
                        name = doc.id,
                        path = doc.get("path") as String,
                        language = getLanguageFromString(doc.id)
                    )
                }
                trySend(BaseClass.Success(fileInfoList))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "An error occurred"))
            }
            awaitClose { }
        }
    }

    override fun getAlgorithm(fileInfo: FileInfo): Flow<BaseClass<Algorithm>> {
        return callbackFlow {
            trySend(BaseClass.Loading)
            try {
                val file = storage.reference.child(fileInfo.path)
                val localFile = File.createTempFile("tempMarkdown", ".md")
                val x = file.getFile(localFile).await()
                if (x != null) {
                    val code = localFile.readText()
                    val language = getLanguageFromString(file.name)
                    val algorithm = Algorithm(
                        id = fileInfo.id,
                        code = code,
                        language = language,
                        title = getFormattedName(
                            getFileName(file.name)
                        ),
                        langName = language.languageGenerics,
                        extension = language.extension
                    )
                    Log.d("FirebaseHelper", "Algorithm: $algorithm")
                    trySend(BaseClass.Success(algorithm))
                } else {
                    trySend(BaseClass.Error("File not found"))
                }
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

    override suspend fun getListOfDocuments(query: String): Flow<BaseClass<List<FileInfo>>> {
        return callbackFlow {
            try {
                trySend(BaseClass.Loading)

                val normalizedQuery = query.replace("_", " ").lowercase()
                val regexQuery = Regex(".*${normalizedQuery.replace(" ", ".*")}.*")

                val response = db.collection("Languages")
                val documents = response.get().await()
                val fileInfoList = mutableListOf<FileInfo>()

                for (document in documents) {
                    val folders = document.get("folders") as List<String>
                    for (collection in folders) {
                        val docRef = db.collection(Const.LANGUAGES).document(document.id)
                            .collection(collection).get().await()
                        for (doc in docRef) {
                            val docName = doc.id.lowercase().replace("_", " ")
                            if (regexQuery.matches(docName) || regexQuery.matches(
                                    doc.getString("content")?.lowercase().orEmpty()
                                )
                            ) {
                                fileInfoList.add(
                                    FileInfo(
                                        id = doc.get("id") as String,
                                        name = doc.id,
                                        path = doc.get("path") as String,
                                        language = getLanguageFromString(doc.id)
                                    )
                                )
                            }
                        }
                    }
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
                val response = db.collection("rosettalang").get().await()
                trySend(BaseClass.Success(response.documents.map {
                    it.id
                }))

            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error"))
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