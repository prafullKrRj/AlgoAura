package com.prafull.algorithms.enrollToAi.howToCreateApiKey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.prafull.algorithms.commons.utils.BaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HowToCreateApiKeyViewModel : ViewModel(), KoinComponent {

    private val db: FirebaseFirestore by inject()
    private val _state = MutableStateFlow<BaseClass<HowToCreateApiKeyState>>(BaseClass.Loading)
    val state = _state.asStateFlow()

    init {
        getSteps()
    }

    fun getSteps() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val doc = db.collection("key").document("howToGetApiKey").get().await()
                    val steps = doc.get("steps") as List<Map<String, Any>>
                    _state.update {
                        BaseClass.Success(
                            HowToCreateApiKeyState(
                                description = doc.getString("description") ?: "",
                                url = doc.getString("url") ?: "",
                                steps = steps.map {
                                    Steps(
                                        title = it["title"] as String,
                                        description = it["description"] as String,
                                        image = it["image"] as String
                                    )
                                }
                            )
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        BaseClass.Error(e.message ?: "Something went wrong")
                    }
                }
            }
        }
    }
}

data class HowToCreateApiKeyState(
    val description: String,
    val url: String,
    val steps: List<Steps>
)

data class Steps(
    val title: String,
    val description: String,
    val image: String
)