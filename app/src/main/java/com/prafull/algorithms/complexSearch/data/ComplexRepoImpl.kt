package com.prafull.algorithms.complexSearch.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.complexSearch.domain.models.ComplexAlgorithm
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageAlgo
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageData
import com.prafull.algorithms.complexSearch.domain.models.ComplexLanguageFiles
import com.prafull.algorithms.complexSearch.domain.repo.ComplexSearchRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class ComplexRepoImpl : ComplexSearchRepo, KoinComponent {
    private val db: FirebaseFirestore by inject()
    private val storage: FirebaseStorage by inject()

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
                trySend(BaseClass.Success(algo))
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
                    extension = doc.get("extension") as String?,
                    langDescription = formatLanguageDescription(doc.get("language") as String?),
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

    fun formatLanguageDescription(description: String?): String {
        if (description == null) return ""
        val keyWords = listOf<String>(
            "See also",
            "External links",
            "References",
            "Further reading",
            "Bibliography",
            "Todo",
            "references",
        )
        var formattedDescription: String = description
        for (keyword in keyWords) {
            val index = formattedDescription.indexOf(keyword, ignoreCase = true)
            if (index != -1) {
                formattedDescription = formattedDescription.substring(0, index).trim()
                break
            }
        }
        formattedDescription.replace("rosetta", "our", ignoreCase = true)
        return formattedDescription
    }

    override suspend fun getComplexLanguageAlgo(
        lang: String, algo: String
    ): Flow<BaseClass<ComplexLanguageAlgo>> {
        return callbackFlow {
            try {
                val doc = db.collection("rosettaAlgos").document(algo).get().await()
                val task = doc.getString("task")
                val stRef = storage.reference.child("Task").child(algo).child(lang)
                val codes = stRef.listAll().await()
                val res = codes.items.map {
                    val localFile = File.createTempFile("tempFile", null)
                    it.getFile(localFile).await()
                    localFile.readText()
                }
                trySend(
                    BaseClass.Success(
                        ComplexLanguageAlgo(
                            algoName = algo, task = formatTask(task ?: ""), langCode = res
                        )
                    )
                )
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "Error", exception = e))
            }
            awaitClose { }
        }
    }

    private fun formatTask(task: String): String {
        val keywords = listOf(
            "similar algos",
            "similar tasks",
            "related tasks",
            "reference",
            "related task",
            "references"
        )
        var formattedTask = task
        for (keyword in keywords) {
            val index = formattedTask.indexOf(keyword, ignoreCase = true)
            if (index != -1) {
                formattedTask = formattedTask.substring(0, index).trim()
                break
            }
        }
        formattedTask.replace("rosetta", "our", ignoreCase = true)
        return formattedTask
    }
}