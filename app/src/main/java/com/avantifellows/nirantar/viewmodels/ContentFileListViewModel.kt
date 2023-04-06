package com.avantifellows.nirantar.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avantifellows.nirantar.ContentFile
import com.avantifellows.nirantar.getFirestoreFileList
import kotlinx.coroutines.launch

class ContentFileListViewModel : ViewModel() {
    private val _fileList = mutableStateListOf<ContentFile>()

    var errorMessage: String by mutableStateOf("")
    val contentFileList: List<ContentFile>
        get() = _fileList

    fun getContentFileListAsync(onFetched: () -> Unit) {
        viewModelScope.launch {
            try {
                _fileList.clear()
                _fileList.addAll(getFirestoreFileList())
                onFetched()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

}