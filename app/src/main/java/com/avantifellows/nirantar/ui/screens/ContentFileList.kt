package com.avantifellows.nirantar.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avantifellows.nirantar.components.ContentFile
import com.avantifellows.nirantar.viewmodels.ContentFileListViewModel

@Composable
fun ContentFileList(vm: ContentFileListViewModel) {
    LaunchedEffect(Unit, block = {
        vm.getContentFileList()
    })
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = vm.contentFileList) { contentFile ->
            ContentFile(contentFile = contentFile)
        }
    }
}
