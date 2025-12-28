package com.example.testownik_mobilny.components.test_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.testownik_mobilny.AppSettings
import com.example.testownik_mobilny.removeImgTag
import com.example.testownik_mobilny.ui.theme.debugOrange
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.lighterDarkGray
import com.example.testownik_mobilny.ui.theme.negativeRed
import com.example.testownik_mobilny.ui.theme.neutralBlue
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.undecidedYellow
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import com.example.testownik_mobilny.view_models.ToggleState
import java.io.File

@Composable
fun TestAnswerButton(
    answer: String,
    id: Int,
    viewModel: TestScreenViewModel,
    directory: File?,
    datastore: AppSettings
) {
    val debugMode by datastore.debugEnable.collectAsState(initial = false)
    val toggled = viewModel.toggledButtons[id]
    val containerColor = when (toggled) {
        ToggleState.TOGGLED -> neutralBlue
        ToggleState.CORRECT -> positiveGreen
        ToggleState.WRONG -> negativeRed
        ToggleState.UNMARKED -> undecidedYellow
        else -> lighterDarkGray
    }

    val roundnessPercent = 15
    val regex = Regex("\\[img].*?\\[/img]", RegexOption.DOT_MATCHES_ALL)
    val containsImg = answer.contains(regex)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.94f)
            .clip(RoundedCornerShape(percent = roundnessPercent))
    ) {
        Button(
            onClick = {
                if (toggled == ToggleState.IDLE || toggled == ToggleState.TOGGLED) {
                    viewModel.toggleButton(id)
                }
            },
            shape = RoundedCornerShape(percent = roundnessPercent),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor.copy(alpha = 0.15f),
                contentColor = whitish
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = containerColor,
                    shape = RoundedCornerShape(percent = roundnessPercent)
                )
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .focusable(true)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!containsImg) {
                    Text(
                        text = answer,
                        fontFamily = googleSansFlex,
                        fontSize = 16.sp,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    val src = regex.find(answer)
                    val img = removeImgTag(src?.groupValues?.get(0).toString())
                    val file = File(directory, img)
                    Image(
                        painter = rememberAsyncImagePainter(file),
                        contentDescription = null,
                        modifier = Modifier
                            .heightIn(min = 60.dp)
                            .fillMaxHeight()
                            .fillMaxWidth(0.8f)
                            .background(whitish)
                            .padding(5.dp)
                    )
                }
            }
        }

        if (debugMode && viewModel.currentQuestion.correctAnswers[id]) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(width = 32.dp, height = 32.dp)
                    .background(
                        color = debugOrange,
                        shape = RoundedCornerShape(bottomStart = 20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = null,
                    tint = whitish,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}