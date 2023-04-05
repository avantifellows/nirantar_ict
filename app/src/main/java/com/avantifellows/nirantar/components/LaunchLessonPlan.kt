
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avantifellows.nirantar.ContentFile
import com.avantifellows.nirantar.utils.DownloadState
import com.avantifellows.nirantar.utils.downloadAndViewFile
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LaunchLessonPlan(selectedLesson: ContentFile, onLaunchPdfClicked: () -> Unit) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf<String?>(null) }
    val TealColor = Color(0xFF008080)
    val context = LocalContext.current
//    var downloadProgress = MutableStateFlow(0)
//    val downloadProgress = remember { mutableStateOf<MutableStateFlow<Int>?>(null) }
//    val progress by downloadProgress.value?.collectAsState(0) ?: remember { mutableStateOf(0) }
    // Move the downloadStatus variable declaration outside the Button onClick
    val downloadStatus = remember { mutableStateOf<StateFlow<DownloadState>?>(null) }
    val downloadState by downloadStatus.value?.collectAsState(DownloadState(0, false)) ?: remember { mutableStateOf(DownloadState(0, false)) }

    val progress = downloadState.progress
    val isDownloadSuccessful = downloadState.isDownloadSuccessful

//    val isDownloadSuccessful = downloadProgress.isDownloadSuccessful
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = selectedLesson.title,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .border(border = BorderStroke(1.dp, TealColor), RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RadioButtonAndText("Teach", selectedOption, onOptionSelected, TealColor)
            RadioButtonAndText("Explore", selectedOption, onOptionSelected, TealColor)
        }

        Button(
            onClick = {
                downloadStatus.value = downloadAndViewFile(
                    selectedLesson.link,
                    context = context,
                    selectedLesson.title
                )
            },
            enabled = selectedOption != null,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Launch PDF")
        }

        if (progress > 0) {
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }

        if (isDownloadSuccessful) {
            Log.d("YO", "PDF Clicked. Moving to completion screen")
            onLaunchPdfClicked()
        }
    }
//

}


@Composable
fun RadioButtonAndText(
    text: String,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    TealColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedOption == text,
            onClick = { onOptionSelected(text) },
            colors = RadioButtonDefaults.colors(
                selectedColor = TealColor,
                unselectedColor = TealColor
            )
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 2.dp, end = 16.dp)
        )
    }
}
