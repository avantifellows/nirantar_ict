package com.avantifellows.nirantar.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

fun viewPdfFile(context: Context, file: File) {
    Log.d("YO", "In View Method. Launching Activity now!")
    Log.d("YO", "")
    val intent = Intent(Intent.ACTION_VIEW)
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
    intent.setDataAndType(pdfUri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // start activity
    context.startActivity(intent)

}