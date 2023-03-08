package com.avantifellows.nirantar.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avantifellows.nirantar.APIService
import com.avantifellows.nirantar.BASE_URL
import com.avantifellows.nirantar.ContentFile
import kotlinx.coroutines.launch

class ContentFileListViewModel : ViewModel() {
    private val _fileList = mutableStateListOf<ContentFile>()
    var errorMessage: String by mutableStateOf("")
    val contentFileList: List<ContentFile>
        get() = _fileList

    fun getContentFileList() {
        Log.d("YO", BASE_URL)
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _fileList.clear()
                _fileList.addAll(apiService.getFileList())
                Log.d("YO", _fileList.toString())
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                Log.d("YO", errorMessage)
            }
        }
    }
}