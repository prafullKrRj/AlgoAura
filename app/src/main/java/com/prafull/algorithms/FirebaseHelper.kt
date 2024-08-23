package com.prafull.algorithms

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent

object FirebaseHelper : KoinComponent {
    private val storage = FirebaseStorage.getInstance()

    fun getFromLanguage(language: ProgrammingLanguage): Flow<BaseClass<List<FolderInfo>>> {
        return callbackFlow {
            try {
                val ref = storage.reference.child(language.fileName)
                val list = ref.listAll().await()
                val folderInfoList = list.prefixes.map { folderRef ->
                    FolderInfo(name = folderRef.name, path = folderRef.path)
                }
                trySend(BaseClass.Success(folderInfoList))
            } catch (e: Exception) {
                trySend(BaseClass.Error(e.message ?: "An error occurred"))
            }
            awaitClose { }
        }
    }

    fun getAlgorithms(path: String): Flow<BaseClass<List<FileInfo>>> {
        return callbackFlow {
            try {
                val files = storage.reference.child(path)
                val list = files.listAll().await()
                val fileInfoList = list.items.map { fileRef ->
                    FileInfo(
                        name = fileRef.name,
                        path = fileRef.path,
                        language = getLanguageFromString(fileRef.name)
                    )
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

fun getLanguageFromString(str: String): ProgrammingLanguage {
    val extension = str.removeSuffix(".md").split(".").last()
    ProgrammingLanguage.entries.forEach { lang ->
        if (lang.extension == extension) {
            return lang
        }
    }
    return ProgrammingLanguage.UNKNOWN
}

sealed interface BaseClass<out T> {
    data class Error(val message: String) : BaseClass<Nothing>
    data class Success<T>(val data: T) : BaseClass<T>
    data object Loading : BaseClass<Nothing>
}