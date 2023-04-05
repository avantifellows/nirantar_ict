package com.avantifellows.nirantar.components

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.avantifellows.nirantar.ContentFile

@Composable
fun ContentFile(contentFile: ContentFile) {
    Card(
        backgroundColor = Color.Transparent,
    ) {
        LessonPlan(contentFile) {}
    }
}