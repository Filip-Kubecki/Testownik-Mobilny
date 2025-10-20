package com.example.testownik_mobilny.components.test_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import com.example.testownik_mobilny.view_models.ToggleState
import com.example.testownik_mobilny.ui.theme.debugOrange
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.neutralBlue
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.ui.theme.negativeRed
import com.example.testownik_mobilny.ui.theme.undecidedYellow

/**
 * Contains answers
 */
@Composable
fun TestAnswerButton(
    answer: String,
    id: Int,
    viewModel: TestScreenViewModel,
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
//    var enabled = !(toggled != ToggleState.IDLE || toggled != ToggleState.TOGGLED)
    var size by remember { mutableStateOf(IntSize.Zero) }
    var roundness = 15
//    val xOffset = -16
//    val yOffset = if(size.height > 70) (-1*size.height/95) else -20

    Button(
        onClick = {
            if (toggled == ToggleState.IDLE){
                viewModel.toggleButton(id)
            }else if(toggled == ToggleState.TOGGLED){
                viewModel.toggleButton(id)
            }
        },
//        enabled = enabled,
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
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
//                .background(whitish.copy(0.1f))
        ){
            Text(
                answer,
                fontFamily = jetBrainsMonoFontFamily,
                fontSize = 16.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
//                    .clip(RoundedCornerShape(percent = roundness))
//            color = whitish,
            )
            if (debugMode && viewModel.currentQuestion.correctAnswers[id]){
                Box(
                    modifier = Modifier
//                        .absoluteOffset(x = xOffset.dp, y = yOffset.dp)
                        .size(30.dp)
                        .clip(RoundedCornerShape(percent = roundness))
                        .background(debugOrange, shape = CircleShape)
                        .align(Alignment.TopStart)
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

