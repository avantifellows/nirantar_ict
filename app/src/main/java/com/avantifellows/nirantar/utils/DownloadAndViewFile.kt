package com.avantifellows.nirantar.utils

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

fun downloadAndViewFile(link: String, context: Context) {
    Log.d("YO", "File downloading: $link")
    val dir = context.filesDir
    val filename = link.substringAfterLast('/').replace(" ", "_")
    Log.d("YO", "Download path: $dir")
    val storage = Firebase.storage
    val gsReference = storage.getReferenceFromUrl(link)
    val rootPath = File(dir, filename)
    if (rootPath.exists()) {
        Log.d("YO", "File already exists")
        viewPdfFile(context, rootPath)
    }
    Log.d("YO", "Root Path: $rootPath")

    gsReference.getFile(rootPath).addOnSuccessListener {
        Log.d("YO", "File created")

    }.addOnFailureListener {
        Log.d("YO", "File not created :(")
    }.addOnProgressListener {
        //calculating progress percentage
        val progress = (100.0 * it.bytesTransferred) / it.totalByteCount;
        //displaying percentage in progress dialog
        Log.d("YO", "Downloaded $progress %")
    }.addOnSuccessListener {
        viewPdfFile(context, rootPath)
    }
}