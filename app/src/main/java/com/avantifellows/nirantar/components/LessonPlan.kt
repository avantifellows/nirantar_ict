package com.avantifellows.nirantar.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.avantifellows.nirantar.ContentFile

val TealColor = Color(0xFF008080)

@Composable
fun LessonPlan(contentFile: ContentFile, onClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, TealColor, RoundedCornerShape(4.dp))
            .background(color = Color.Transparent)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = contentFile.title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(4.dp)
                    .weight(0.8f)
            )

            Text(
                text = contentFile.chapterCode,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(4.dp)
                    .weight(0.2f)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = contentFile.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Button(
                onClick = onClick,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = TealColor,
                    contentColor = Color.White
                )
            ) {
                Text("View Lesson")
            }

        }
    }
}

