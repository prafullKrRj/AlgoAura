package com.prafull.algorithms.screens.folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.algorithms.data.FirebaseHelper
import com.prafull.algorithms.models.FileInfo
import com.prafull.algorithms.models.FolderInfo
import com.prafull.algorithms.utils.BaseClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FolderViewModel : ViewModel() {

    private var cachedFolders by mutableStateOf(CachedState())

    private val _files = MutableStateFlow<BaseClass<List<FileInfo>>>(BaseClass.Loading)
    val files = _files.asStateFlow()

    fun getFies(folder: String) {
        if (cachedFolders.folders.any { it.folder.path == folder }) {
            val cachedFolder = cachedFolders.folders.first { it.folder.path == folder }
            _files.update {
                BaseClass.Success(cachedFolder.files)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                FirebaseHelper.getAlgorithms(path = folder).collectLatest { resp ->
                    _files.update {
                        resp
                    }
                    if (resp is BaseClass.Success) {
                        cachedFolders = cachedFolders.copy(
                            folders = cachedFolders.folders + CachedFolder(
                                folder = FolderInfo(name = folder, path = folder),
                                files = resp.data
                            )
                        )
                    }
                }
            }
        }

    }
}

data class CachedState(
    val folders: Set<CachedFolder> = emptySet()
)

data class CachedFolder(
    val folder: FolderInfo,
    val files: List<FileInfo> = emptyList()
)