package com.example.testownik_mobilny.components.test_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import com.example.testownik_mobilny.ui.theme.positiveGreen

/**
 * Let's user check their answers and navigate to next question
 */
@Composable
fun ConfirmChoiceButton(
    viewModel: TestScreenViewModel,
    modifier: Modifier = Modifier,
    iconDescription: String = "Icon",
    rotation: Float = 0f,
){
    var isToggled by remember { mutableStateOf(false) }
    Button(
        onClick = {
            if (isToggled == false){
                isToggled = true
                viewModel.checkAnswers()
            }else{
                isToggled = false
                viewModel.confirmButtonCheck()
            }
        }
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = positiveGreen
        ),
        modifier = modifier
            .size(75.dp, 75.dp)
    ){
        Image(
            painter = painterResource(if(isToggled) R.drawable.right_icon else R.drawable.confirm_icon),
            contentDescription = iconDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.rotate(rotation)
        )
    }
}
