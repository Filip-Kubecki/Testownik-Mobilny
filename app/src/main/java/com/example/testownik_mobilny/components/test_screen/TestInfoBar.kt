package com.example.testownik_mobilny.components.test_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lightGray
import com.example.testownik_mobilny.ui.theme.whitish
import kotlinx.coroutines.delay
import java.util.Locale

/**
 * Contains information's about test:
 *
 * time spend on current test
 *
 * number of unanswered, answered
 * and memorized questions
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestInfoBar(
    viewModel: TestScreenViewModel
){
    var timeElapsed by remember { mutableIntStateOf(0) }

    val localTextStyle = TextStyle(
        fontFamily = jetBrainsMonoFontFamily,
        fontSize = 16.sp,
        color = whitish
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeElapsed++
            viewModel.testInformation.timeSpent = timeElapsed
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(top = 15.dp, bottom = 5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.88f)
        ) {viewModel.testInformation.memorizedQuestions
            Text(
                "Liczba pytań: ${viewModel.testInformation.memorizedQuestions.size}/${viewModel.testInformation.answeredQuestions.size}/${viewModel.testInformation.numberOfQuestions}",
                style = localTextStyle
            )
            Text(
                formatTime(timeElapsed),
                style = localTextStyle
            )
        }
        Spacer(modifier = Modifier.height(2.dp).fillMaxWidth(0.96f).background(lightGray))
    }
}

fun formatTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds)
}