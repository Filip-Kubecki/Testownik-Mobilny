package com.example.testownik_mobilny.Components

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.TestScreenViewModel
import com.example.testownik_mobilny.ToggleState
import com.example.testownik_mobilny.ui.theme.debugOrange
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.neutralBlue
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.ui.theme.negativeRed
import com.example.testownik_mobilny.ui.theme.undecidedYellow

@Composable
fun TestAnswerButton(
    answer: String,
    id: Int,
    viewModel: TestScreenViewModel,
    debugMode: Boolean = false,
    modifier: Modifier = Modifier
){
    val toggled = viewModel.toggledButtons[id]
    var containerColor = when(toggled){
        ToggleState.TOGGLED -> neutralBlue
        ToggleState.CORRECT -> positiveGreen
        ToggleState.WRONG -> negativeRed
        ToggleState.UNMARKED -> undecidedYellow
        else -> {lighterGray}
    }
    var enabled = !(toggled != ToggleState.IDLE || toggled != ToggleState.TOGGLED)

    Button(
        onClick = {
            if (toggled == ToggleState.IDLE){
                viewModel.toggleButton(id)
            }else if(toggled == ToggleState.TOGGLED){
                viewModel.toggleButton(id)
            }
        },
//        enabled = enabled,
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor.copy(alpha = 0.15f),
            contentColor = whitish
        ),
        modifier = Modifier
            .border(2.dp,
                containerColor,
                shape = RoundedCornerShape(25))
            .fillMaxWidth(0.94f)
            .height(100.dp)
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Text(
                answer,
                fontFamily = jetBrainsMonoFontFamily,
                fontSize = 22.sp,
                lineHeight = 30.sp
//            color = whitish,
            )
            if (debugMode && viewModel.currentQuestion.correctAnswers[id]){
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(debugOrange, shape = CircleShape)
                        .align(Alignment.TopEnd)
//                        .border(1.dp, shape = CircleShape, color = negativeRed.copy(0.4f))
                ){
                    Image(
                        painter = painterResource(R.drawable.debug_icon),
                        contentDescription = "Debug",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.scale(0.65f)
                    )
                }
            }
        }
    }
}

enum class ButtonState {
    TOGGLED, CORRECT, WRONG
}