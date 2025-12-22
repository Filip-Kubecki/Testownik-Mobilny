package com.example.testownik_mobilny.components.test_screen

import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.removeImgTag
import com.example.testownik_mobilny.ui.theme.debugOrange
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.negativeRed
import com.example.testownik_mobilny.ui.theme.neutralBlue
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.undecidedYellow
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import com.example.testownik_mobilny.view_models.ToggleState
import java.io.File

/**
 * Contains answers
 */
@Composable
fun TestAnswerButton(
    answer: String,
    id: Int,
    viewModel: TestScreenViewModel,
    directory: File?,
    debugMode: Boolean = false
){
    val toggled = viewModel.toggledButtons[id]
    var containerColor = when(toggled){
        ToggleState.TOGGLED -> neutralBlue
        ToggleState.CORRECT -> positiveGreen
        ToggleState.WRONG -> negativeRed
        ToggleState.UNMARKED -> undecidedYellow
        else -> {lighterGray}
    }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var focusable by remember { mutableStateOf(true) }
    var roundness = 15

//    Check if there is [img] tag in the answer
    var regex = Regex("\\[img].*?\\[/img]", RegexOption.DOT_MATCHES_ALL)
    var containsImg = answer.contains(regex)

    Button(
        onClick = {
            if (toggled == ToggleState.IDLE){
                viewModel.toggleButton(id)
            }else if(toggled == ToggleState.TOGGLED){
                viewModel.toggleButton(id)
            }
        },
        shape = RoundedCornerShape(percent = roundness),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor.copy(alpha = 0.15f),
            contentColor = whitish
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .border(2.dp,
                containerColor,
                shape = RoundedCornerShape(percent = roundness))
            .fillMaxWidth(0.94f)
            .heightIn(min = 80.dp)
            .onGloballyPositioned{ cord ->
                size = cord.size
            }
            .focusable(focusable)
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ){
            if(!containsImg){
                Text(
                    answer,
                    fontFamily = jetBrainsMonoFontFamily,
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }else{
                var src = regex.find(answer)
                var img = removeImgTag(src?.groupValues?.get(0).toString())
                val file = File(directory, img)
                Log.d("SELF", "Source: $file")
                Image(
//                    painterResource(R.drawable.ic_launcher_background),
                    rememberAsyncImagePainter(file),
                    contentDescription = "img",
                    modifier = Modifier
                        .heightIn(min = 60.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .background(whitish)
                )
            }

//            DEBUG MODE
            if (debugMode && viewModel.currentQuestion.correctAnswers[id]){
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(percent = roundness))
                        .background(debugOrange, shape = CircleShape)
                        .align(Alignment.TopEnd)
                ){
                    Image(
                        painter = painterResource(R.drawable.debug_icon),
                        contentDescription = "Debug",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .scale(0.65f)
                    )
                }
            }
        }
    }
}
