package com.avantifellows.nirantar.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avantifellows.nirantar.ContentFile
import com.avantifellows.nirantar.utils.downloadAndViewFile

@Composable
fun ContentFile(contentFile: ContentFile) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(contentFile, ::downloadAndViewFile)
    }
}