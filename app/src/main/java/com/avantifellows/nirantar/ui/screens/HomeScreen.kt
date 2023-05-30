package com.avantifellows.nirantar.ui.screens

import LaunchLessonPlan
import LessonCompletionScreen
import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.avantifellows.nirantar.ContentFile
import com.avantifellows.nirantar.components.LessonPlan
import com.avantifellows.nirantar.viewmodels.ContentFileListViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import java.time.Duration
import java.time.Instant

@Composable
fun HomeScreen(
    vm: ContentFileListViewModel,
    modifier: Modifier
) {
    var selectedLesson by remember { mutableStateOf<ContentFile?>(null) }
    val context = LocalContext.current
    var currentScreenState by remember { mutableStateOf("home") }


    // Check and set active lesson from shared preferences
    fun checkAndSetActiveLesson() {
        Log.d("YO", "Checking Shared preferences")
        val sharedPreferences = context.getSharedPreferences("viewed_lessons", Context.MODE_PRIVATE)
        for (contentFile in vm.contentFileList) {
            Log.d("YO", contentFile.title)
            if (sharedPreferences.getBoolean(contentFile.title, false)) {
                selectedLesson = contentFile
                currentScreenState = "doneWithLesson"
                break
            }
        }
    }

    LaunchedEffect(Unit, block = {
        vm.getContentFileListAsync() { checkAndSetActiveLesson() }

    })

    when (currentScreenState) {
        "home" -> {
            LazyColumn(
                modifier = modifier
            ) {
                items(items = vm.contentFileList) { contentFile ->
                    LessonPlan(contentFile) {
                        selectedLesson = contentFile
                        currentScreenState = "launchLesson"
                    }
                }
            }
        }
        "launchLesson" -> {
            if (selectedLesson != null) {
                Log.d("YO", "PDF not launched")
                Log.d("YO", "File Clicked! ${selectedLesson!!.title}")

                LaunchLessonPlan(selectedLesson = selectedLesson!!)
                {
                    currentScreenState = "doneWithLesson"
                }
            }
        }
        "doneWithLesson" -> {
            if (selectedLesson != null) {
                val sharedPreferences =
                    context.getSharedPreferences("viewed_lessons", Context.MODE_PRIVATE)
                val sessionId = sharedPreferences.getString("session_id", "")
                val startTime = sharedPreferences.getLong("timestamp", 0)
                val currentTime = Instant.now().toEpochMilli()  // Get current time in milliseconds
                val duration = Duration.between(
                    Instant.ofEpochMilli(startTime),
                    Instant.ofEpochMilli(currentTime)
                ).toSeconds()
                LessonCompletionScreen(
                    contentFile = selectedLesson!!,
                    sessionId = sessionId!!,
                    duration = duration
                ) {
                    Log.d("YO", "Duration: $duration")
                    val analytics = FirebaseAnalytics.getInstance(context)
                    analytics.logEvent("pdf_close") {
                        param(FirebaseAnalytics.Param.ITEM_NAME, selectedLesson!!.title)
                        param(FirebaseAnalytics.Param.CONTENT_TYPE, "pdf")
                        param("subject_name", selectedLesson!!.subjectName)
                        param("subject_code", selectedLesson!!.subjectCode)
                        param("chapter_code", selectedLesson!!.chapterCode)
                        param("chapter_name", selectedLesson!!.chapterName)
                        param("session_id", sessionId)
                        param("timestamp", currentTime)
                        param("duration", duration)
                        // Reset the SharedPreferences value for the viewed lesson
                        sharedPreferences.edit().putBoolean(selectedLesson!!.title, false).apply()
                        sharedPreferences.edit().remove("session_id").apply()
//                        currentScreenState = "home"
                    }
                }
            }
        }
    }
}

