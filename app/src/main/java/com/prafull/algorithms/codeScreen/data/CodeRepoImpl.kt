package com.prafull.algorithms.codeScreen.data

import com.google.firebase.firestore.FirebaseFirestore
import com.prafull.algorithms.codeScreen.domain.CodeRepository
import com.prafull.algorithms.commons.models.Algorithm
import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.utils.BaseClass
import com.prafull.algorithms.commons.utils.Const
import com.prafull.algorithms.commons.utils.getFileName
import com.prafull.algorithms.commons.utils.getFormattedNameExtension
import com.prafull.algorithms.commons.utils.getLanguageFromString
import com.prafull.algorithms.favourites.data.local.FavAlgoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CodeRepoImpl : CodeRepository, KoinComponent {
    private val dao: FavAlgoDao by inject()
    private val db: FirebaseFirestore by inject()

    override suspend fun checkIfAlgoExists(id: String): Boolean {
        return dao.checkIfAlgoExists(id).isNotEmpty()
    }

    override suspend fun toggle(algorithm: Algorithm) {
        if (checkIfAlgoExists(algorithm.id)) {
            delete(algorithm)
        } else {
            insert(algorithm)
        }
    }

    override suspend fun insert(algo: Algorithm) = dao.insert(algo.toEntity())
    private suspend fun delete(algo: Algorithm) = dao.delete(algo.toEntity())


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
}