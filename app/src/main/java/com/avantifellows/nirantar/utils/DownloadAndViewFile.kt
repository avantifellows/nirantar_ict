package com.avantifellows.nirantar.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

fun downloadAndViewFile(
    link: String,
    context: Context,
    fileTitle: String
): StateFlow<DownloadState> {
    val downloadProgress = MutableStateFlow(DownloadState(0, false))

    val downloadSuccess = MutableLiveData<Boolean>()

    Log.d("YO", "File downloading: $link")
    val dir = context.filesDir
    val filename = link.substringAfterLast('/').replace(" ", "_")
    Log.d("YO", "Download path: $dir")
    val storage = Firebase.storage
    val gsReference = storage.getReferenceFromUrl(link)
    val rootPath = File(dir, filename)
    if (rootPath.exists()) {
        Log.d("YO", "File already exists")
        val sharedPreferences = context.getSharedPreferences("viewed_lessons", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(fileTitle, true).apply()
        downloadProgress.value = DownloadState(100, true)
        viewPdfFile(context, rootPath, fileTitle)
    } else {
        Log.d("YO", "Root Path: $rootPath")

        gsReference.getFile(rootPath).addOnSuccessListener {
            Log.d("YO", "File created")
        }.addOnFailureListener {
            Log.d("YO", "File not created :(")
            downloadSuccess.postValue(false)
        }.addOnProgressListener {
            val progress = ((100.0 * it.bytesTransferred) / it.totalByteCount).toInt()
            downloadProgress.value = DownloadState(progress, false)
            Log.d("YO", "Downloaded $progress %")
        }.addOnSuccessListener {
            val sharedPreferences =
                context.getSharedPreferences("viewed_lessons", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(fileTitle, true).apply()
            downloadProgress.value = DownloadState(100, true)
            viewPdfFile(context, rootPath, fileTitle)
        }
    }
    return downloadProgress

}



