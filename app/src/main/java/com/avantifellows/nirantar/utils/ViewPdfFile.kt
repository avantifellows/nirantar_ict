package com.avantifellows.nirantar.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.avantifellows.nirantar.ContentFile
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import java.io.File
import java.time.Instant
import java.util.*

fun generate16CharUUID(): String {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 16)
}

fun viewPdfFile(context: Context, file: File, fileToOpen: ContentFile) {
    Log.d("YO", "In View Method. Launching Activity now!")
    Log.d("YO", "")
    val intent = Intent(Intent.ACTION_VIEW)
    val fileTitle = fileToOpen.title
    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    Log.d(
        "YO", context.applicationContext
            .packageName
    )
    val pdfUri = FileProvider.getUriForFile(
        context,
        context.applicationContext
            .packageName + ".provider", file
    )

    Log.d("YO", pdfUri.toString())
    val sessionId = generate16CharUUID()
    val sharedPreferences = context.getSharedPreferences("viewed_lessons", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(fileTitle, true).apply()
    sharedPreferences.edit().putString("session_id", sessionId).apply()

    val currentTime = Instant.now().toEpochMilli()  // Get current time in milliseconds
    Log.d("YO", "Start time: $currentTime")
    sharedPreferences.edit().putLong("timestamp", currentTime).apply()
    // Write to Firebase analytics
    val analytics = FirebaseAnalytics.getInstance(context)
    analytics.logEvent("pdf_open") {
        param(FirebaseAnalytics.Param.ITEM_NAME, fileTitle)
        param(FirebaseAnalytics.Param.CONTENT_TYPE, "pdf")
        param("subject_name", fileToOpen.subjectName)
        param("subject_code", fileToOpen.subjectCode)
        param("chapter_code", fileToOpen.chapterCode)
        param("chapter_name", fileToOpen.chapterName)
        param("session_id", sessionId)
        param("timestamp", currentTime)
    }
    intent.setDataAndType(pdfUri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // start activity
    context.startActivity(intent)

}