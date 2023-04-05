package com.avantifellows.nirantar.ui.screens

import LaunchLessonPlan
import LessonCompletionScreen
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.avantifellows.nirantar.ContentFile
import com.avantifellows.nirantar.components.LessonPlan
import com.avantifellows.nirantar.viewmodels.ContentFileListViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    vm: ContentFileListViewModel
) {
    var selectedLesson by remember { mutableStateOf<ContentFile?>(null) }
    val context = LocalContext.current
    var currentScreenState by remember { mutableStateOf("home") }
    var downloadProgress by remember { mutableStateOf<StateFlow<Int>?>(null) }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit, block = {
        vm.getContentFileList()
    })

    when (currentScreenState) {
        "home" -> {
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
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
                LessonCompletionScreen(contentFile = selectedLesson!!) {
                    currentScreenState = "home"

                    // Reset the SharedPreferences value for the viewed lesson
                    val sharedPreferences =
                        context.getSharedPreferences("viewed_lessons", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean(selectedLesson!!.title, false).apply()
                }
            }
        }
    }
}
